package com.remindly.app.ui.screens.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.automirrored.filled.PhoneForwarded
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.remindly.app.R
import com.remindly.app.permissions.PermissionUtils
import com.remindly.app.ui.components.PermissionDialog
import com.remindly.app.ui.components.PrimaryButton
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showNotificationDialog by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { _ ->
        onFinished()
    }

    fun requestNotificationPermissionThenFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !PermissionUtils.hasNotificationPermission(context)
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onFinished()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppSpacing.xl),
    ) {
        Spacer(Modifier.weight(0.5f))

        Text(
            text = stringResource(R.string.onboarding_headline),
            style = MaterialTheme.typography.displaySmall,
        )

        Spacer(Modifier.padding(top = AppSpacing.xxl))

        OnboardingFeatureRow(
            icon = Icons.Filled.NotificationsActive,
            title = stringResource(R.string.onboarding_feature_alerts_title),
            description = stringResource(R.string.onboarding_feature_alerts_desc),
        )
        Spacer(Modifier.padding(top = AppSpacing.lg))
        OnboardingFeatureRow(
            icon = Icons.AutoMirrored.Filled.PhoneForwarded,
            title = stringResource(R.string.onboarding_feature_call_title),
            description = stringResource(R.string.onboarding_feature_call_desc),
        )

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.onboarding_cta),
            onClick = { showNotificationDialog = true },
        )

        Spacer(Modifier.padding(top = AppSpacing.md))

        LegalFooter(onOpenPrivacyPolicy = onOpenPrivacyPolicy)
    }

    if (showNotificationDialog) {
        PermissionDialog(
            title = stringResource(R.string.notif_dialog_title),
            body = stringResource(R.string.notif_dialog_body),
            onConfirm = {
                showNotificationDialog = false
                requestNotificationPermissionThenFinish()
            },
        )
    }
}

@Composable
private fun OnboardingFeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp),
        )
        Spacer(Modifier.padding(start = AppSpacing.md))
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.extendedColors.textSecondary,
            )
        }
    }
}

@Composable
private fun LegalFooter(onOpenPrivacyPolicy: () -> Unit) {
    val fullText = stringResource(R.string.onboarding_legal)
    val privacyWord = stringResource(R.string.onboarding_privacy_policy)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = fullText,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.extendedColors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.padding(top = AppSpacing.xxs))
        ClickableTextLink(privacyWord, onOpenPrivacyPolicy)
    }
}

@Composable
private fun ClickableTextLink(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .padding(top = 2.dp)
            .clickable(onClick = onClick),
    )
}
