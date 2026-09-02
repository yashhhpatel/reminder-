package com.remindly.app.ui.screens.newreminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.domain.model.SoundMode
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.AppCard
import com.remindly.app.ui.components.AppDatePickerDialog
import com.remindly.app.ui.components.AppSwitch
import com.remindly.app.ui.components.AppTimePickerDialog
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.components.CategoryDot
import com.remindly.app.ui.components.PremiumBadge
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun NewReminderScreen(
    factory: RemindlyViewModelFactory,
    reminderId: Long?,
    isPremium: Boolean,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    onOpenRepeat: () -> Unit,
    onOpenCategory: () -> Unit,
    onOpenLocationPicker: () -> Unit,
    onOpenPremium: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderEditorViewModel,
) {
    LaunchedEffect(reminderId) { viewModel.load(reminderId) }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showSoundMenu by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = stringResource(if (state.isEditMode) R.string.edit_reminder_title else R.string.new_reminder_title),
            onBack = onBack,
            actions = {
                IconButton(onClick = { viewModel.save() }, enabled = state.isValid) {
                    Icon(Icons.Filled.Check, contentDescription = stringResource(R.string.reminder_save))
                }
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::updateTitle,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.reminder_memo_hint)) },
                minLines = 2,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(),
            )

            AppCard {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.reminder_add_time), style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    AppSwitch(checked = state.addTimeEnabled, onCheckedChange = viewModel::toggleAddTime)
                }

                if (state.addTimeEnabled && state.dateTime != null) {
                    Spacer(Modifier.padding(top = AppSpacing.sm))
                    HorizontalDivider()
                    Spacer(Modifier.padding(top = AppSpacing.sm))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            formatDateTimeLabel(state.dateTime!!),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .weight(1f)
                                .clickableSimple { showDatePicker = true },
                        )
                        IconButton(onClick = viewModel::clearDateTime) {
                            Icon(
                                Icons.Filled.RemoveCircle,
                                contentDescription = stringResource(R.string.reminder_remove_time),
                                tint = AppTheme.extendedColors.destructive,
                            )
                        }
                    }

                    Spacer(Modifier.padding(top = AppSpacing.xs))
                    QuickTimePresetRow(stringResource(R.string.reminder_suggestion_1hour)) {
                        viewModel.setDateTime(Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }.timeInMillis)
                    }
                    QuickTimePresetRow(stringResource(R.string.reminder_suggestion_today_7am)) {
                        viewModel.setDateTime(todayAt(7, 0))
                    }
                    QuickTimePresetRow(stringResource(R.string.reminder_suggestion_today_3pm)) {
                        viewModel.setDateTime(todayAt(15, 0))
                    }
                    QuickTimePresetRow(stringResource(R.string.reminder_suggestion_today_10pm)) {
                        viewModel.setDateTime(todayAt(22, 0))
                    }

                    Spacer(Modifier.padding(top = AppSpacing.xs))
                    HorizontalDivider()

                    RowActionItem(
                        icon = Icons.Filled.Repeat,
                        label = repeatLabel(state.repeatType),
                        onClick = onOpenRepeat,
                    )
                    androidx.compose.foundation.layout.Box {
                        RowActionItem(
                            icon = Icons.Filled.VolumeUp,
                            label = soundLabel(state.soundMode),
                            onClick = { showSoundMenu = true },
                        )
                        DropdownMenu(expanded = showSoundMenu, onDismissRequest = { showSoundMenu = false }) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.reminder_sound_once)) },
                                onClick = { viewModel.setSound(SoundMode.RING_ONCE); showSoundMenu = false },
                                leadingIcon = { RadioButton(selected = state.soundMode == SoundMode.RING_ONCE, onClick = null) },
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.reminder_sound_repeat)) },
                                onClick = { viewModel.setSound(SoundMode.REPEAT_UNTIL_DISMISSED); showSoundMenu = false },
                                leadingIcon = { RadioButton(selected = state.soundMode == SoundMode.REPEAT_UNTIL_DISMISSED, onClick = null) },
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.reminder_sound_silent)) },
                                onClick = { viewModel.setSound(SoundMode.SILENT); showSoundMenu = false },
                                leadingIcon = { RadioButton(selected = state.soundMode == SoundMode.SILENT, onClick = null) },
                            )
                        }
                    }
                }
            }

            AppCard(
                premiumBorder = !isPremium,
                onClick = {
                    if (!isPremium) {
                        onOpenPremium()
                    } else if (!state.placeEnabled) {
                        onOpenLocationPicker()
                    }
                },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.reminder_add_place), style = MaterialTheme.typography.titleMedium)
                    if (!isPremium) {
                        Spacer(Modifier.width(AppSpacing.xxs))
                        PremiumBadge(size = 16.dp)
                    }
                    Spacer(Modifier.weight(1f))
                    AppSwitch(
                        checked = state.placeEnabled && isPremium,
                        onCheckedChange = { enabled ->
                            if (!isPremium) {
                                onOpenPremium()
                            } else if (enabled) {
                                onOpenLocationPicker()
                            } else {
                                viewModel.clearPlace()
                            }
                        },
                    )
                }
                if (state.placeEnabled && state.placeName != null) {
                    Text(
                        state.placeName.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.extendedColors.textSecondary,
                    )
                } else {
                    Text(
                        stringResource(R.string.reminder_add_place_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.extendedColors.textSecondary,
                    )
                }
            }

            AppCard(onClick = onOpenCategory) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    CategoryDot(state.category?.colorArgb ?: MaterialTheme.colorScheme.primary.toArgb())
                    Spacer(Modifier.width(AppSpacing.sm))
                    Text(
                        state.category?.name ?: stringResource(R.string.category_my_reminders),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(Icons.Filled.ChevronRight, contentDescription = null)
                }
            }

            if (state.isEditMode && !state.addTimeEnabled) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.NotificationsOff,
                        contentDescription = null,
                        tint = AppTheme.extendedColors.textSecondary,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(AppSpacing.xxs))
                    Text(
                        stringResource(R.string.reminder_no_notification_warning),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.extendedColors.textSecondary,
                    )
                }
            }

            Spacer(Modifier.padding(top = AppSpacing.xxl))
        }
    }

    if (showDatePicker && state.dateTime != null) {
        AppDatePickerDialog(
            initialMillis = state.dateTime!!,
            onConfirm = {
                viewModel.setDateTime(mergeDateKeepTime(it, state.dateTime!!))
                showDatePicker = false
                showTimePicker = true
            },
            onDismiss = { showDatePicker = false },
        )
    }
    if (showTimePicker && state.dateTime != null) {
        val cal = Calendar.getInstance().apply { timeInMillis = state.dateTime!! }
        AppTimePickerDialog(
            initialHour = cal.get(Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(Calendar.MINUTE),
            is24Hour = false,
            onConfirm = { hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                viewModel.setDateTime(cal.timeInMillis)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false },
        )
    }
}

@Composable
private fun QuickTimePresetRow(label: String, onClick: () -> Unit) {
    Text(
        label,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickableSimple(onClick)
            .padding(vertical = AppSpacing.xs),
    )
}

@Composable
private fun RowActionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableSimple(onClick)
            .padding(vertical = AppSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.width(AppSpacing.sm))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun Modifier.clickableSimple(onClick: () -> Unit): Modifier =
    this.clickable(onClick = onClick)

@Composable
private fun repeatLabel(type: com.remindly.app.domain.model.RepeatType): String = when (type) {
    com.remindly.app.domain.model.RepeatType.NONE -> stringResource(R.string.reminder_repeat_none)
    com.remindly.app.domain.model.RepeatType.DAILY -> stringResource(R.string.reminder_repeat_daily)
    com.remindly.app.domain.model.RepeatType.WEEKDAY -> stringResource(R.string.reminder_repeat_weekday)
    com.remindly.app.domain.model.RepeatType.WEEKLY -> stringResource(R.string.reminder_repeat_weekly)
    com.remindly.app.domain.model.RepeatType.MONTHLY -> stringResource(R.string.reminder_repeat_monthly)
    com.remindly.app.domain.model.RepeatType.YEARLY -> stringResource(R.string.reminder_repeat_yearly)
    com.remindly.app.domain.model.RepeatType.CUSTOM -> stringResource(R.string.reminder_repeat_custom)
}

@Composable
private fun soundLabel(mode: SoundMode): String = when (mode) {
    SoundMode.RING_ONCE -> stringResource(R.string.reminder_sound_once)
    SoundMode.REPEAT_UNTIL_DISMISSED -> stringResource(R.string.reminder_sound_repeat)
    SoundMode.SILENT -> stringResource(R.string.reminder_sound_silent)
}

private fun todayAt(hour: Int, minute: Int): Long = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
    set(Calendar.SECOND, 0)
}.timeInMillis

private fun mergeDateKeepTime(newDateMillis: Long, oldMillis: Long): Long {
    val newCal = Calendar.getInstance().apply { timeInMillis = newDateMillis }
    val oldCal = Calendar.getInstance().apply { timeInMillis = oldMillis }
    newCal.set(Calendar.HOUR_OF_DAY, oldCal.get(Calendar.HOUR_OF_DAY))
    newCal.set(Calendar.MINUTE, oldCal.get(Calendar.MINUTE))
    return newCal.timeInMillis
}

private fun formatDateTimeLabel(millis: Long): String {
    val target = Calendar.getInstance().apply { timeInMillis = millis }
    val now = Calendar.getInstance()
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val dayLabel = when {
        target.get(Calendar.YEAR) == now.get(Calendar.YEAR) && target.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) -> "Today"
        else -> {
            val tomorrow = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
            if (target.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) && target.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
                "Tomorrow"
            } else {
                SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(target.time)
            }
        }
    }
    return "$dayLabel · ${timeFormat.format(target.time)}"
}
