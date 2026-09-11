package com.remindly.app.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.AppCard
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.util.AppLocale
import com.remindly.app.util.findActivity

@Composable
fun LanguageScreen(
    factory: RemindlyViewModelFactory,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onConfirm: (() -> Unit)? = null,
    viewModel: SettingsViewModel = viewModel(factory = factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val isOnboarding = onConfirm != null
    val context = LocalContext.current

    fun selectLanguage(code: String) {
        if (code == uiState.language) {
            if (!isOnboarding) onBack()
            return
        }
        viewModel.setLanguage(code)
        AppLocale.apply(context, code)
        // Navigation Compose's back stack survives recreate() the same way it survives a
        // rotation (rememberSaveable), so this lands back on this exact screen — mid-onboarding
        // or mid-Settings — now rendered in the new language, instead of losing the user's place.
        context.findActivity()?.recreate()
    }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = stringResource(R.string.language_title),
            onBack = if (isOnboarding) null else onBack,
            actions = {
                if (isOnboarding) {
                    IconButton(onClick = onConfirm!!) {
                        Icon(Icons.Filled.Check, contentDescription = stringResource(R.string.language_confirm_cd))
                    }
                }
            },
        )
        AppCard(
            modifier = Modifier
                .padding(AppSpacing.md)
                .verticalScroll(rememberScrollState()),
        ) {
            SupportedLanguages.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectLanguage(option.code) }
                        .padding(vertical = AppSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(option.displayName, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    RadioButton(selected = uiState.language == option.code, onClick = { selectLanguage(option.code) })
                }
                if (index != SupportedLanguages.lastIndex) HorizontalDivider()
            }
        }
    }
}
