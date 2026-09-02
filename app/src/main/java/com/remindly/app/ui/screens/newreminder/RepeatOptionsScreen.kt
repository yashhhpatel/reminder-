package com.remindly.app.ui.screens.newreminder

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
import com.remindly.app.R
import com.remindly.app.domain.model.RepeatType
import com.remindly.app.ui.components.AppCard
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.theme.AppSpacing

private data class RepeatOption(val type: RepeatType, val labelRes: Int)

private val repeatOptions = listOf(
    RepeatOption(RepeatType.NONE, R.string.reminder_repeat_none),
    RepeatOption(RepeatType.DAILY, R.string.reminder_repeat_daily),
    RepeatOption(RepeatType.WEEKDAY, R.string.reminder_repeat_weekday),
    RepeatOption(RepeatType.WEEKLY, R.string.reminder_repeat_weekly),
    RepeatOption(RepeatType.MONTHLY, R.string.reminder_repeat_monthly),
    RepeatOption(RepeatType.YEARLY, R.string.reminder_repeat_yearly),
)

@Composable
fun RepeatOptionsScreen(
    viewModel: ReminderEditorViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.repeat_title), onBack = onBack)
        AppCard(modifier = Modifier.padding(AppSpacing.md)) {
            repeatOptions.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setRepeat(option.type)
                            onBack()
                        }
                        .padding(vertical = AppSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(option.labelRes), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    RadioButton(selected = state.repeatType == option.type, onClick = {
                        viewModel.setRepeat(option.type)
                        onBack()
                    })
                }
                if (index != repeatOptions.lastIndex) HorizontalDivider()
            }
        }
    }
}
