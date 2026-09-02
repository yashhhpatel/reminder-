package com.remindly.app.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.AppCard
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.theme.AppSpacing

private data class LanguageOption(val code: String, val labelRes: Int)

private val supportedLanguages = listOf(
    LanguageOption("en", R.string.language_english),
)

@Composable
fun LanguageScreen(
    factory: RemindlyViewModelFactory,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = factory),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.language_title), onBack = onBack)
        AppCard(modifier = Modifier.padding(AppSpacing.md)) {
            supportedLanguages.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setLanguage(option.code); onBack() }
                        .padding(vertical = AppSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(option.labelRes), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    RadioButton(selected = uiState.language == option.code, onClick = { viewModel.setLanguage(option.code); onBack() })
                }
                if (index != supportedLanguages.lastIndex) HorizontalDivider()
            }
        }
    }
}
