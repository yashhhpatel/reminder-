package com.remindly.app.ui.screens.privacy

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.permissions.PermissionUtils
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.components.SettingsToggleRow
import com.remindly.app.ui.theme.AppSpacing

@Composable
fun PrivacySettingsScreen(
    factory: RemindlyViewModelFactory,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PrivacySettingsViewModel = viewModel(factory = factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(PermissionUtils.hasNotificationPermission(context)) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted -> notificationsEnabled = granted }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.privacy_settings_title), onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        ) {
            SettingsToggleRow(
                title = stringResource(R.string.privacy_notifications),
                subtitle = stringResource(R.string.privacy_notifications_desc),
                icon = Icons.Filled.Notifications,
                checked = notificationsEnabled,
                onCheckedChange = { enabled ->
                    if (enabled) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            notificationsEnabled = true
                        }
                    } else {
                        context.startActivity(PermissionUtils.appSettingsIntent(context))
                    }
                },
            )
            SettingsToggleRow(
                title = stringResource(R.string.privacy_location),
                subtitle = stringResource(R.string.privacy_location_desc),
                icon = Icons.Filled.LocationOn,
                checked = uiState.locationEnabled,
                onCheckedChange = { enabled ->
                    viewModel.setLocationEnabled(enabled)
                    if (enabled && !PermissionUtils.hasFineLocationPermission(context)) {
                        context.startActivity(PermissionUtils.appSettingsIntent(context))
                    }
                },
            )
            SettingsToggleRow(
                title = stringResource(R.string.privacy_analytics),
                subtitle = stringResource(R.string.privacy_analytics_desc),
                icon = Icons.Filled.Analytics,
                checked = uiState.analyticsEnabled,
                enabled = false,
                onCheckedChange = {},
            )
            SettingsToggleRow(
                title = stringResource(R.string.privacy_personalized),
                subtitle = stringResource(R.string.privacy_personalized_desc),
                icon = Icons.Filled.Tune,
                checked = uiState.personalizedContentEnabled,
                enabled = false,
                onCheckedChange = {},
            )
        }
    }
}
