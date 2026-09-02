package com.remindly.app.ui.screens.onboarding

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.remindly.app.R
import com.remindly.app.permissions.PermissionUtils
import com.remindly.app.ui.components.PrimaryButton
import com.remindly.app.ui.components.SecondaryButton
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme

@Composable
fun OverlayExplainerScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var overlayGranted by remember { mutableStateOf(PermissionUtils.hasOverlayPermission(context)) }

    val phonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) {
        context.startActivity(PermissionUtils.overlaySettingsIntent(context))
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                overlayGranted = PermissionUtils.hasOverlayPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppSpacing.xl),
    ) {
        Spacer(Modifier.weight(0.3f))

        Text(
            text = stringResource(R.string.overlay_explainer_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(Modifier.padding(top = AppSpacing.sm))
        Text(
            text = stringResource(R.string.overlay_explainer_body),
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.extendedColors.textSecondary,
        )

        Spacer(Modifier.padding(top = AppSpacing.xl))

        MockPermissionFrame(overlayGranted)

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.overlay_go_to_settings),
            onClick = {
                if (!PermissionUtils.hasPhoneStatePermission(context)) {
                    phonePermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
                } else {
                    context.startActivity(PermissionUtils.overlaySettingsIntent(context))
                }
            },
        )
        Spacer(Modifier.padding(top = AppSpacing.sm))
        SecondaryButton(text = stringResource(R.string.continue_action), onClick = onContinue)
    }
}

@Composable
private fun MockPermissionFrame(granted: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, AppTheme.extendedColors.cardBorder, RoundedCornerShape(24.dp))
            .padding(AppSpacing.lg),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                Icons.Filled.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp),
            )
            Spacer(Modifier.padding(start = AppSpacing.sm))
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier,
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = if (granted) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                contentDescription = if (granted) "Enabled" else "Disabled",
                tint = if (granted) MaterialTheme.colorScheme.primary else AppTheme.extendedColors.textSecondary,
            )
        }
    }
}
