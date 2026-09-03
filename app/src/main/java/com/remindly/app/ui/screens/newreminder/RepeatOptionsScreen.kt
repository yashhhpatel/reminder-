package com.remindly.app.ui.screens.newreminder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.remindly.app.R
import com.remindly.app.domain.model.RepeatType
import com.remindly.app.ui.components.AppCard
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.components.PrimaryButton
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme
import java.util.Calendar

private data class RepeatOption(val type: RepeatType, val labelRes: Int)

private val repeatOptions = listOf(
    RepeatOption(RepeatType.NONE, R.string.reminder_repeat_none),
    RepeatOption(RepeatType.DAILY, R.string.reminder_repeat_daily),
    RepeatOption(RepeatType.WEEKDAY, R.string.reminder_repeat_weekday),
    RepeatOption(RepeatType.WEEKLY, R.string.reminder_repeat_weekly),
    RepeatOption(RepeatType.MONTHLY, R.string.reminder_repeat_monthly),
    RepeatOption(RepeatType.YEARLY, R.string.reminder_repeat_yearly),
    RepeatOption(RepeatType.CUSTOM, R.string.reminder_repeat_custom),
)

private val dayLabels = listOf(
    Calendar.SUNDAY to "S",
    Calendar.MONDAY to "M",
    Calendar.TUESDAY to "T",
    Calendar.WEDNESDAY to "W",
    Calendar.THURSDAY to "T",
    Calendar.FRIDAY to "F",
    Calendar.SATURDAY to "S",
)

@Composable
fun RepeatOptionsScreen(
    viewModel: ReminderEditorViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    var selectedType by remember { mutableStateOf(state.repeatType) }
    var selectedDays by remember { mutableStateOf(state.repeatDays) }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.repeat_title), onBack = onBack)
        AppCard(
            modifier = Modifier
                .padding(AppSpacing.md)
                .verticalScroll(rememberScrollState()),
        ) {
            repeatOptions.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedType = option.type
                            if (option.type != RepeatType.CUSTOM) {
                                viewModel.setRepeat(option.type)
                                onBack()
                            }
                        }
                        .padding(vertical = AppSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(option.labelRes), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    RadioButton(selected = selectedType == option.type, onClick = {
                        selectedType = option.type
                        if (option.type != RepeatType.CUSTOM) {
                            viewModel.setRepeat(option.type)
                            onBack()
                        }
                    })
                }
                if (index != repeatOptions.lastIndex) HorizontalDivider()
            }

            if (selectedType == RepeatType.CUSTOM) {
                HorizontalDivider()
                Column(modifier = Modifier.padding(top = AppSpacing.md)) {
                    Text(
                        stringResource(R.string.reminder_repeat_custom_days_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.extendedColors.textSecondary,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppSpacing.sm),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        dayLabels.forEach { (day, label) ->
                            DayToggle(
                                label = label,
                                selected = day in selectedDays,
                                onClick = {
                                    selectedDays = if (day in selectedDays) selectedDays - day else selectedDays + day
                                },
                            )
                        }
                    }
                    Spacer_(AppSpacing.md)
                    PrimaryButton(
                        text = stringResource(R.string.save),
                        enabled = selectedDays.isNotEmpty(),
                        onClick = {
                            viewModel.setRepeat(RepeatType.CUSTOM, selectedDays)
                            onBack()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun Spacer_(size: androidx.compose.ui.unit.Dp) {
    androidx.compose.foundation.layout.Spacer(Modifier.padding(top = size))
}

@Composable
private fun DayToggle(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
