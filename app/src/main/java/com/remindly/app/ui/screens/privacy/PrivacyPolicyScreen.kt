package com.remindly.app.ui.screens.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.remindly.app.R
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.privacy_policy_title), onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppSpacing.md),
        ) {
            PolicySection(
                "What we store",
                "All reminders, categories, and settings are stored locally on your device using an encrypted Android app-private database. We do not upload your reminders to any server.",
            )
            PolicySection(
                "Notifications",
                "We request notification permission so Remindly can alert you when a reminder is due. If you deny this permission, the app still works — you just won't receive alerts, and you can grant permission later from your device Settings.",
            )
            PolicySection(
                "Location",
                "Location access is only used for place-based reminders you create yourself (\"Add place\"). Your location is never transmitted anywhere; it is only compared on-device against the places you've set.",
            )
            PolicySection(
                "Geofencing",
                "If you enable a place-based reminder, Remindly registers a geofence with Android's location services so it can notify you when you arrive. This runs entirely on-device via Google Play services.",
            )
            PolicySection(
                "Phone state (after-call reminders)",
                "If enabled, Remindly reads only the call *state* (idle/ringing/off-hook) to detect when a call has ended, so it can offer to create a reminder. We never record, transcribe, or store call audio or call content, and we do not access your call log.",
            )
            PolicySection(
                "Analytics",
                "This build of Remindly does not collect any analytics or usage data.",
            )
            PolicySection(
                "Data deletion",
                "Deleting a reminder or category removes it immediately and permanently from the local database. Uninstalling the app removes all of its data.",
            )
        }
    }
}

@Composable
private fun PolicySection(title: String, body: String) {
    Text(title, style = MaterialTheme.typography.titleLarge)
    Text(
        body,
        style = MaterialTheme.typography.bodyMedium,
        color = AppTheme.extendedColors.textSecondary,
        modifier = Modifier.padding(top = AppSpacing.xxs, bottom = AppSpacing.lg),
    )
}
