package com.remindly.app.ui.screens.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.PrimaryButton
import com.remindly.app.ui.theme.AppDimens
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme
import com.remindly.app.ui.theme.BrandPurple
import com.remindly.app.ui.theme.BrandPurpleLight
import com.remindly.app.ui.theme.PaywallAccentGreen
import com.remindly.app.ui.theme.PaywallBackground
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private enum class PremiumPlan(
    val titleRes: Int,
    val amountRes: Int,
    val periodRes: Int,
    val trialDays: Int,
) {
    YEARLY(R.string.premium_plan_yearly_title, R.string.premium_plan_yearly_amount, R.string.premium_plan_yearly_period, 7),
    MONTHLY(R.string.premium_plan_monthly_title, R.string.premium_plan_monthly_amount, R.string.premium_plan_monthly_period, 5),
    WEEKLY(R.string.premium_plan_weekly_title, R.string.premium_plan_weekly_amount, R.string.premium_plan_weekly_period, 3),
}

@Composable
fun PremiumScreen(
    factory: RemindlyViewModelFactory,
    onClose: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PremiumViewModel = viewModel(factory = factory),
) {
    val purchaseCompleted by viewModel.purchaseCompleted.collectAsState()
    var selectedPlan by remember { mutableStateOf(PremiumPlan.YEARLY) }
    var showTrialOffer by remember { mutableStateOf(false) }

    LaunchedEffect(purchaseCompleted) {
        if (purchaseCompleted) onClose()
    }

    if (showTrialOffer) {
        TrialOfferScreen(
            plan = selectedPlan,
            onClose = onClose,
            onStartTrial = { viewModel.continuePurchase() },
            onOpenTerms = onOpenTerms,
            onOpenPrivacy = onOpenPrivacy,
            onRestore = { viewModel.restore() },
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaywallBackground)
            .padding(AppSpacing.lg),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = { showTrialOffer = true }) {
                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.close), tint = Color.White)
            }
        }

        Spacer(Modifier.padding(top = AppSpacing.md))

        Text(
            stringResource(R.string.premium_headline),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.padding(top = AppSpacing.sm))
        Box(
            modifier = Modifier
                .size(width = 48.dp, height = 3.dp)
                .background(BrandPurpleLight),
        )

        Spacer(Modifier.padding(top = AppSpacing.xl))

        BenefitRow(stringResource(R.string.premium_feature_places))
        BenefitRow(stringResource(R.string.premium_feature_ads))
        BenefitRow(stringResource(R.string.premium_feature_support))

        Spacer(Modifier.weight(1f))

        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                PremiumPlan.entries.forEach { plan ->
                    PlanCard(
                        plan = plan,
                        selected = plan == selectedPlan,
                        onClick = { selectedPlan = plan },
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 0.dp, end = AppSpacing.md)
                    .offset(y = (-12).dp)
                    .clip(RoundedCornerShape(50))
                    .background(BrandPurple)
                    .padding(horizontal = AppSpacing.sm, vertical = 4.dp),
            ) {
                Text(
                    stringResource(R.string.premium_plan_badge),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(Modifier.padding(top = AppSpacing.lg))

        PrimaryButton(
            text = stringResource(R.string.premium_continue),
            onClick = { viewModel.continuePurchase() },
            containerColor = PaywallAccentGreen,
        )

        Spacer(Modifier.padding(top = AppSpacing.sm))
        Text(
            stringResource(R.string.premium_renew_note),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.Shield,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.padding(start = 4.dp))
            Text(
                stringResource(R.string.premium_cancel_note),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )
        }

        Spacer(Modifier.padding(top = AppSpacing.md))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            FooterLink(stringResource(R.string.premium_terms), onOpenTerms)
            FooterLink(stringResource(R.string.premium_privacy), onOpenPrivacy)
            FooterLink(stringResource(R.string.premium_restore), onClick = { viewModel.restore() })
        }
    }
}

/**
 * Shown when the user tries to close the main paywall instead of subscribing — a one-time
 * downsell offering a free trial sized to whichever plan they had selected (matches the
 * reference app's exit-intent flow). Since there is no real billing wired up, "starting" the
 * trial just grants the mocked premium flag the same way the main paywall's Continue does.
 */
@Composable
private fun TrialOfferScreen(
    plan: PremiumPlan,
    onClose: () -> Unit,
    onStartTrial: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayFormat = remember { SimpleDateFormat("d MMM", Locale.getDefault()) }
    val notifyDate = remember(plan) {
        dayFormat.format((Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }).time)
    }
    val memberDate = remember(plan) {
        dayFormat.format((Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, plan.trialDays) }).time)
    }
    val amount = stringResource(plan.amountRes)
    val period = stringResource(plan.periodRes)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(AppSpacing.lg),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.close))
            }
        }

        Text(
            stringResource(R.string.trial_headline),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            stringResource(R.string.trial_subheadline, plan.trialDays),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.padding(top = AppSpacing.sm))
        Box(modifier = Modifier.size(width = 48.dp, height = 3.dp).background(MaterialTheme.colorScheme.primary))

        Spacer(Modifier.padding(top = AppSpacing.xl))
        Text(stringResource(R.string.trial_how_it_works), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = AppSpacing.md))

        TrialStep(
            emoji = "✅",
            title = stringResource(R.string.trial_step_start_title),
            body = stringResource(R.string.trial_step_start_body, plan.trialDays),
        )
        TrialStep(
            emoji = "💬",
            title = stringResource(R.string.trial_step_notify_title, notifyDate),
            body = stringResource(R.string.trial_step_notify_body),
        )
        TrialStep(
            emoji = "❤️",
            title = stringResource(R.string.trial_step_member_title, memberDate),
            body = stringResource(R.string.trial_step_member_body),
            showConnector = false,
        )

        Spacer(Modifier.weight(1f))

        Text(
            stringResource(R.string.trial_price_line, plan.trialDays, amount, period),
            style = MaterialTheme.typography.bodyLarge,
            color = AppTheme.extendedColors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.padding(top = AppSpacing.sm))

        PrimaryButton(
            text = stringResource(R.string.trial_cta, plan.trialDays),
            onClick = onStartTrial,
            containerColor = PaywallAccentGreen,
        )

        Spacer(Modifier.padding(top = AppSpacing.sm))
        Text(
            stringResource(R.string.premium_renew_note),
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.extendedColors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.Shield,
                contentDescription = null,
                tint = AppTheme.extendedColors.textSecondary,
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.padding(start = 4.dp))
            Text(
                stringResource(R.string.premium_cancel_note),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.extendedColors.textSecondary,
            )
        }
        Spacer(Modifier.padding(top = AppSpacing.md))
        val footerColor = AppTheme.extendedColors.textSecondary
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            FooterLink(stringResource(R.string.premium_terms), onOpenTerms, footerColor)
            FooterLink(stringResource(R.string.premium_privacy), onOpenPrivacy, footerColor)
            FooterLink(stringResource(R.string.premium_restore), onRestore, footerColor)
        }
    }
}

@Composable
private fun TrialStep(emoji: String, title: String, body: String, showConnector: Boolean = true) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Text(emoji, style = MaterialTheme.typography.bodyMedium)
            }
            if (showConnector) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .size(width = 2.dp, height = 32.dp)
                        .background(MaterialTheme.colorScheme.outline),
                )
            }
        }
        Spacer(Modifier.padding(start = AppSpacing.sm))
        Column(modifier = Modifier.padding(bottom = AppSpacing.sm)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(body, style = MaterialTheme.typography.bodySmall, color = AppTheme.extendedColors.textSecondary)
        }
    }
}

@Composable
private fun PlanCard(plan: PremiumPlan, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimens.cardCornerRadiusSmall))
            .background(if (selected) BrandPurple.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.06f))
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) BrandPurple else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(AppDimens.cardCornerRadiusSmall),
            )
            .clickable(onClick = onClick)
            .padding(AppSpacing.md),
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(stringResource(plan.titleRes), color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Text(
            stringResource(R.string.premium_plan_price_per, stringResource(plan.amountRes), stringResource(plan.periodRes)),
            color = BrandPurpleLight,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun BenefitRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White)
        Spacer(Modifier.padding(start = AppSpacing.sm))
        Text(text, color = Color.White, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun FooterLink(text: String, onClick: () -> Unit, color: Color = Color.White.copy(alpha = 0.6f)) {
    Text(
        text,
        color = color,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.clickable(onClick = onClick),
    )
}
