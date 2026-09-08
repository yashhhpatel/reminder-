package com.remindly.app.ui.screens.onboarding

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.remindly.app.R
import com.remindly.app.permissions.PermissionUtils
import com.remindly.app.ui.components.PrimaryButton
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme

@Composable
fun PrivacyConsentScreen(
    onFinished: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { _ -> onFinished() }

    fun requestLocationThenFinish() {
        if (!PermissionUtils.hasFineLocationPermission(context)) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            onFinished()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.xl),
    ) {
        Text(
            text = stringResource(R.string.privacy_consent_headline),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.padding(top = AppSpacing.lg))
        Text(
            text = stringResource(R.string.privacy_consent_subheading),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.padding(top = AppSpacing.sm))
        Text(
            text = stringResource(R.string.privacy_consent_body),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.padding(top = AppSpacing.lg))
        Text(
            text = stringResource(R.string.privacy_consent_collect_heading),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.padding(top = AppSpacing.sm))
        ConsentBullet(stringResource(R.string.privacy_consent_bullet_1))
        ConsentBullet(stringResource(R.string.privacy_consent_bullet_2))
        ConsentBullet(stringResource(R.string.privacy_consent_bullet_3))
        ConsentBullet(stringResource(R.string.privacy_consent_bullet_4))
        ConsentBullet(stringResource(R.string.privacy_consent_bullet_5))

        Spacer(Modifier.padding(top = AppSpacing.lg))
        val finePrintPrefix = stringResource(R.string.privacy_consent_fine_print) + " "
        val privacyPolicyLink = stringResource(R.string.onboarding_privacy_policy)
        val finePrintText = buildAnnotatedString {
            withStyle(SpanStyle(color = AppTheme.extendedColors.textSecondary)) {
                append(finePrintPrefix)
            }
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(privacyPolicyLink)
            }
        }
        ClickableText(
            text = finePrintText,
            style = MaterialTheme.typography.bodySmall,
            onClick = { offset -> if (offset >= finePrintPrefix.length) onOpenPrivacyPolicy() },
        )

        Spacer(Modifier.padding(top = AppSpacing.xl))

        PrimaryButton(
            text = stringResource(R.string.privacy_consent_accept),
            onClick = { requestLocationThenFinish() },
        )
        Spacer(Modifier.padding(top = AppSpacing.sm))
        PrimaryButton(
            text = stringResource(R.string.privacy_consent_decline),
            onClick = onFinished,
        )
        Spacer(Modifier.padding(bottom = AppSpacing.md))
    }
}

@Composable
private fun ConsentBullet(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = AppSpacing.xxs)) {
        Text("•  ", style = MaterialTheme.typography.bodyMedium)
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.extendedColors.textSecondary,
        )
    }
}
