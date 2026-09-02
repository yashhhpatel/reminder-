package com.remindly.app.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.domain.model.Category
import com.remindly.app.domain.model.Reminder
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.AppSwitch
import com.remindly.app.ui.components.BottomQuickAdd
import com.remindly.app.ui.components.EmptyReminderState
import com.remindly.app.ui.components.LoadingState
import com.remindly.app.ui.components.ReminderCard
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(
    factory: RemindlyViewModelFactory,
    onAddReminder: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenReminder: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = factory),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.home_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            if (!uiState.isEmpty) {
                IconButton(onClick = onOpenSearch) {
                    Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.search))
                }
            }
            IconButton(onClick = onOpenSettings) {
                Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings))
            }
        }

        when {
            uiState.isLoading -> LoadingState(modifier = Modifier.weight(1f))
            uiState.isEmpty -> EmptyReminderState(onAddReminder = onAddReminder, modifier = Modifier.weight(1f))
            else -> {
                ReminderList(
                    upcoming = uiState.upcoming,
                    completed = uiState.completed,
                    onOpenReminder = onOpenReminder,
                    onToggleComplete = { reminder, completed -> viewModel.setCompleted(reminder, completed) },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        BottomQuickAdd(
            onSubmit = { text, dateTime ->
                if (dateTime != null || uiState.isEmpty) {
                    viewModel.quickAddWithTime(text, dateTime)
                } else {
                    viewModel.quickAdd(text)
                }
            },
        )
    }
}

@Composable
private fun ReminderList(
    upcoming: List<Reminder>,
    completed: List<Reminder>,
    onOpenReminder: (Long) -> Unit,
    onToggleComplete: (Reminder, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var soonExpanded by remember { mutableStateOf(true) }
    var completedExpanded by remember { mutableStateOf(true) }

    val noDueDate = stringResource(R.string.home_no_due_date)
    val completedLabel = stringResource(R.string.home_completed_label)
    val completedPrefix = stringResource(R.string.home_completed_prefix)
    val soonTitle = stringResource(R.string.home_section_soon)
    val completedTitle = stringResource(R.string.home_section_completed)

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = AppSpacing.md, vertical = AppSpacing.xs),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xs),
    ) {
        if (upcoming.isNotEmpty()) {
            item {
                SectionHeader(
                    title = soonTitle,
                    expanded = soonExpanded,
                    onToggle = { soonExpanded = !soonExpanded },
                )
            }
            if (soonExpanded) {
                items(upcoming, key = { "u_${it.id}" }) { reminder ->
                    ReminderCard(
                        reminder = reminder,
                        subtitle = reminder.dateTime?.let { formatDueLabel(it) } ?: noDueDate,
                        categoryColor = reminder.color ?: MaterialTheme.colorScheme.primary.toArgbSafe(),
                        onClick = { onOpenReminder(reminder.id) },
                        onToggleComplete = { checked -> onToggleComplete(reminder, checked) },
                    )
                }
            }
        }
        if (completed.isNotEmpty()) {
            item {
                Spacer(Modifier.padding(top = AppSpacing.xs))
                SectionHeader(
                    title = completedTitle,
                    expanded = completedExpanded,
                    onToggle = { completedExpanded = !completedExpanded },
                )
            }
            if (completedExpanded) {
                items(completed, key = { "c_${it.id}" }) { reminder ->
                    ReminderCard(
                        reminder = reminder,
                        subtitle = reminder.completedAt?.let { String.format(completedPrefix, formatDueLabel(it)) } ?: completedLabel,
                        categoryColor = reminder.color ?: MaterialTheme.colorScheme.primary.toArgbSafe(),
                        onClick = { onOpenReminder(reminder.id) },
                        onToggleComplete = { checked -> onToggleComplete(reminder, checked) },
                    )
                }
            }
        }
        item { Spacer(Modifier.padding(top = AppSpacing.xxl)) }
    }
}

@Composable
private fun SectionHeader(title: String, expanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            title,
            style = MaterialTheme.typography.labelLarge,
            color = AppTheme.extendedColors.textSecondary,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onToggle) {
            Icon(
                Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                modifier = if (!expanded) Modifier.rotate(180f) else Modifier,
            )
        }
    }
}

private fun formatDueLabel(millis: Long): String {
    val target = Calendar.getInstance().apply { timeInMillis = millis }
    val now = Calendar.getInstance()
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val dayLabel = when {
        isSameDay(target, now) -> "Today"
        isSameDay(target, (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }) -> "Tomorrow"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(target.time)
    }
    return "$dayLabel · ${timeFormat.format(target.time)}"
}

private fun isSameDay(a: Calendar, b: Calendar): Boolean =
    a.get(Calendar.YEAR) == b.get(Calendar.YEAR) && a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)

private fun androidx.compose.ui.graphics.Color.toArgbSafe(): Int = this.toArgb()
