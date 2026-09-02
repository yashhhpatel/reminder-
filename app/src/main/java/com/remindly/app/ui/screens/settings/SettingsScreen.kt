package com.remindly.app.ui.screens.settings

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feedback
import androidx.compose.material.icons.automirrored.filled.Share
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.remindly.app.BuildConfig
import com.remindly.app.R
import com.remindly.app.domain.model.AppThemeMode
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.components.GoProBanner
import com.remindly.app.ui.components.SettingsRow
import com.remindly.app.ui.components.SettingsSectionLabel
import com.remindly.app.ui.components.SettingsToggleRow
import com.remindly.app.ui.theme.AppSpacing
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SettingsScreen(
    factory: RemindlyViewModelFactory,
    onBack: () -> Unit,
    onOpenLanguage: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    onOpenPrivacySettings: () -> Unit,
    onOpenPremium: () -> Unit,
    onOpenFeedback: () -> Unit,
    onShare: () -> Unit,
    onRateUs: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.settings_title), onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        ) {
            if (!uiState.isPremium) {
                GoProBanner(onGoPro = onOpenPremium)
            }

            SettingsSectionLabel(stringResource(R.string.settings_section_general))
            SettingsRow(
                title = stringResource(R.string.settings_language),
                icon = Icons.Filled.Language,
                valueText = stringResource(R.string.language_english),
                onClick = onOpenLanguage,
            )
            SettingsRow(
                title = stringResource(R.string.settings_theme),
                icon = Icons.Filled.Brightness6,
                valueText = themeLabel(uiState.themeMode),
                onClick = { showThemeDialog = true },
            )

            SettingsSectionLabel(stringResource(R.string.settings_section_presets))
            SettingsToggleRow(
                title = stringResource(R.string.settings_add_time),
                subtitle = stringResource(R.string.settings_add_time_desc),
                icon = Icons.Filled.Schedule,
                checked = uiState.presetAddTime,
                onCheckedChange = viewModel::setPresetAddTime,
            )
            SettingsToggleRow(
                title = stringResource(R.string.settings_add_place),
                subtitle = stringResource(R.string.settings_add_place_desc),
                icon = Icons.Filled.Lock,
                premium = !uiState.isPremium,
                checked = uiState.presetAddPlace && uiState.isPremium,
                onCheckedChange = { enabled ->
                    if (!uiState.isPremium) onOpenPremium() else viewModel.setPresetAddPlace(enabled)
                },
            )
            SettingsToggleRow(
                title = stringResource(R.string.settings_after_call),
                subtitle = stringResource(R.string.settings_after_call_desc),
                icon = Icons.Filled.Phone,
                checked = uiState.presetAfterCall,
                onCheckedChange = viewModel::setPresetAfterCall,
            )

            SettingsSectionLabel(stringResource(R.string.settings_section_communicate))
            SettingsRow(
                title = stringResource(R.string.settings_feedback),
                icon = Icons.AutoMirrored.Filled.Feedback,
                onClick = onOpenFeedback,
            )
            SettingsRow(
                title = stringResource(R.string.settings_privacy_policy),
                icon = Icons.Filled.PrivacyTip,
                onClick = onOpenPrivacyPolicy,
            )
            SettingsRow(
                title = stringResource(R.string.settings_privacy_settings),
                icon = Icons.Filled.Shield,
                onClick = onOpenPrivacySettings,
            )

            SettingsSectionLabel(stringResource(R.string.settings_section_others))
            SettingsRow(
                title = stringResource(R.string.settings_share),
                icon = Icons.AutoMirrored.Filled.Share,
                onClick = onShare,
            )
            SettingsRow(
                title = stringResource(R.string.settings_rate_us),
                icon = Icons.Filled.Star,
                onClick = onRateUs,
            )

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = AppSpacing.lg), horizontalArrangement = Arrangement.Center) {
                Text(
                    stringResource(R.string.settings_app_version, BuildConfig.VERSION_NAME),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

    if (showThemeDialog) {
        AppThemeDialog(
            current = uiState.themeMode,
            onSelect = { viewModel.setThemeMode(it) },
            onDismiss = { showThemeDialog = false },
        )
    }
}

@Composable
private fun AppThemeDialog(
    current: AppThemeMode,
    onSelect: (AppThemeMode) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_theme)) },
        text = {
            Column {
                ThemeOptionRow(stringResource(R.string.settings_theme_auto), selected == AppThemeMode.AUTO) { selected = AppThemeMode.AUTO }
                ThemeOptionRow(stringResource(R.string.settings_theme_light), selected == AppThemeMode.LIGHT) { selected = AppThemeMode.LIGHT }
                ThemeOptionRow(stringResource(R.string.settings_theme_dark), selected == AppThemeMode.DARK) { selected = AppThemeMode.DARK }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSelect(selected); onDismiss() }) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
    )
}

@Composable
private fun ThemeOptionRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = AppSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun themeLabel(mode: AppThemeMode): String = when (mode) {
    AppThemeMode.AUTO -> stringResource(R.string.settings_theme_auto)
    AppThemeMode.LIGHT -> stringResource(R.string.settings_theme_light)
    AppThemeMode.DARK -> stringResource(R.string.settings_theme_dark)
}
