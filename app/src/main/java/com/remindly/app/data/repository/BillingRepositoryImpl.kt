package com.remindly.app.data.repository

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.remindly.app.data.datastore.SettingsDataStore
import com.remindly.app.domain.model.PremiumProductIds
import com.remindly.app.domain.model.SubscriptionProduct
import com.remindly.app.domain.repository.PremiumRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Google Play Billing–backed entitlement. Entitlement is cached in [SettingsDataStore] so
 * [isPremium] answers instantly; that cache is refreshed from Play on connect, after every
 * purchase update, and on [restorePurchases]. No backend exists to verify purchase signatures
 * server-side, so a purchase is trusted and acknowledged as soon as Play reports it as PURCHASED
 * — acceptable for a solo/indie app, but add server-side verification if fraud becomes a concern.
 */
class BillingRepositoryImpl(
    context: Context,
    private val settingsDataStore: SettingsDataStore,
) : PremiumRepository {

    private val appContext = context.applicationContext
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _products = MutableStateFlow<List<SubscriptionProduct>>(emptyList())
    override val products: Flow<List<SubscriptionProduct>> = _products.asStateFlow()

    override val isPremium: Flow<Boolean> = settingsDataStore.isPremium

    @Volatile
    private var productDetailsById: Map<String, ProductDetails> = emptyMap()

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            purchases?.forEach { handlePurchase(it) }
        }
    }

    private val billingClient: BillingClient = BillingClient.newBuilder(appContext)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    init {
        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails()
                    scope.launch { refreshEntitlement() }
                }
            }

            override fun onBillingServiceDisconnected() {
                scope.launch {
                    delay(RECONNECT_DELAY_MS)
                    startConnection()
                }
            }
        })
    }

    private fun queryProductDetails() {
        val products = PremiumProductIds.ALL.map { id ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder().setProductList(products).build()
        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) return@queryProductDetailsAsync
            productDetailsById = productDetailsList.associateBy { it.productId }
            _products.value = productDetailsList.mapNotNull { it.toSubscriptionProduct() }
        }
    }

    private fun ProductDetails.toSubscriptionProduct(): SubscriptionProduct? {
        val phase = subscriptionOfferDetails?.firstOrNull()
            ?.pricingPhases?.pricingPhaseList?.lastOrNull()
            ?: return null
        return SubscriptionProduct(
            productId = productId,
            formattedPrice = phase.formattedPrice,
            billingPeriod = friendlyPeriod(phase.billingPeriod),
        )
    }

    private fun friendlyPeriod(isoDuration: String): String = when (isoDuration) {
        "P1Y" -> "year"
        "P1M" -> "month"
        "P1W" -> "week"
        else -> isoDuration
    }

    override fun launchPurchaseFlow(activity: Activity, productId: String) {
        val productDetails = productDetailsById[productId] ?: return
        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: return
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken)
                        .build(),
                ),
            )
            .build()
        billingClient.launchBillingFlow(activity, params)
    }

    override suspend fun restorePurchases(): Boolean = refreshEntitlement()

    private suspend fun refreshEntitlement(): Boolean {
        val purchases = queryActiveSubscriptionPurchases()
        purchases.forEach { handlePurchase(it) }
        val hasActive = purchases.any { it.purchaseState == Purchase.PurchaseState.PURCHASED }
        settingsDataStore.setPremium(hasActive)
        return hasActive
    }

    private suspend fun queryActiveSubscriptionPurchases(): List<Purchase> =
        suspendCancellableCoroutine { continuation ->
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
            billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
                val result = if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) purchases else emptyList()
                if (continuation.isActive) continuation.resume(result)
            }
        }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return
        scope.launch { settingsDataStore.setPremium(true) }
        if (!purchase.isAcknowledged) {
            val ackParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(ackParams) { }
        }
    }

    private companion object {
        const val RECONNECT_DELAY_MS = 3000L
    }
}
