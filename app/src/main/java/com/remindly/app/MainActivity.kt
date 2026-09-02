package com.remindly.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.review.ReviewManagerFactory
import com.remindly.app.notification.NotificationHelper
import com.remindly.app.receiver.PhoneCallReceiver
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.navigation.RemindlyNavGraph
import com.remindly.app.ui.navigation.Routes
import com.remindly.app.ui.theme.RemindlyTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as RemindlyApp).container
        val factory = RemindlyViewModelFactory(container)

        var keepSplash = true
        splashScreen.setKeepOnScreenCondition { keepSplash }

        val initialReminderId = intent.getLongExtra(NotificationHelper.EXTRA_REMINDER_ID, -1L)
        val initialAfterCall = intent.action == PhoneCallReceiver.ACTION_AFTER_CALL

        setContent {
            var startDestination by remember { mutableStateOf<String?>(null) }
            var pendingReminderId by remember { mutableStateOf(initialReminderId) }
            var pendingAfterCall by remember { mutableStateOf(initialAfterCall) }

            LaunchedEffect(Unit) {
                val onboardingDone = container.settingsDataStore.onboardingDone.first()
                startDestination = if (onboardingDone) Routes.HOME else Routes.ONBOARDING
                keepSplash = false
            }

            val themeMode by container.settingsDataStore.themeMode.collectAsStateWithLifecycle(
                initialValue = com.remindly.app.domain.model.AppThemeMode.AUTO,
            )

            val destination = startDestination
            if (destination != null) {
                RemindlyTheme(themeMode = themeMode) {
                    val navController = rememberNavController()

                    LaunchedEffect(pendingReminderId, destination) {
                        if (pendingReminderId > 0 && destination == Routes.HOME) {
                            navController.navigate(Routes.reminderDetail(pendingReminderId))
                            pendingReminderId = -1L
                        }
                    }
                    LaunchedEffect(pendingAfterCall, destination) {
                        if (pendingAfterCall && destination == Routes.HOME) {
                            navController.navigate(Routes.editorGraph(0L))
                            pendingAfterCall = false
                        }
                    }

                    val isPremium by container.premiumRepository.isPremium.collectAsState(initial = false)

                    RemindlyNavGraph(
                        navController = navController,
                        factory = factory,
                        startDestination = destination,
                        isPremium = isPremium,
                        onOpenPrivacyPolicyExternal = { navController.navigate(Routes.PRIVACY_POLICY) },
                        onOpenFeedback = { openFeedback() },
                        onShare = { openShareSheet() },
                        onRateUs = { openRateUs() },
                        onOnboardingComplete = {
                            lifecycleScope.launch { container.settingsDataStore.setOnboardingDone(true) }
                        },
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        recreate()
    }

    private fun openFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_body, BuildConfig.VERSION_NAME))
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            android.app.AlertDialog.Builder(this)
                .setTitle(R.string.feedback_no_email_title)
                .setMessage(R.string.feedback_no_email_body)
                .setPositiveButton(R.string.ok, null)
                .show()
        }
    }

    private fun openShareSheet() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)))
    }

    private fun openRateUs() {
        runCatching {
            val reviewManager = ReviewManagerFactory.create(this)
            val request = reviewManager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reviewManager.launchReviewFlow(this, task.result)
                } else {
                    showRateFallback()
                }
            }
        }.onFailure { showRateFallback() }
    }

    private fun showRateFallback() {
        android.app.AlertDialog.Builder(this)
            .setTitle(R.string.rate_fallback_title)
            .setMessage(R.string.rate_fallback_body)
            .setPositiveButton(R.string.ok, null)
            .show()
    }
}
