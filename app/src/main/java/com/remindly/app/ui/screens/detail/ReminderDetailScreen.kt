package com.remindly.app.ui.screens.detail

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.remindly.app.R
import com.remindly.app.domain.model.RepeatType
import com.remindly.app.domain.model.SoundMode
import com.remindly.app.ui.components.AppCard
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.components.CategoryDot
import com.remindly.app.ui.components.ConfirmationDialog
import com.remindly.app.ui.screens.newreminder.ReminderEditorViewModel
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ReminderDetailScreen(
    reminderId: Long,
    viewModel: ReminderEditorViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(reminderId) { viewModel.load(reminderId) }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onDeleted()
    }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = "", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppSpacing.md)
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        ) {
            AppCard {
                Text(
                    state.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textDecoration = if (state.isCompleted) TextDecoration.LineThrough else null,
                )
                if (state.isCompleted) {
                    Text(
                        "Completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.extendedColors.textSecondary,
                    )
                }
            }

            if (state.dateTime != null) {
                AppCard {
                    Text("Time", style = MaterialTheme.typography.labelMedium, color = AppTheme.extendedColors.textSecondary)
                    Spacer(Modifier.padding(top = AppSpacing.xs))
                    DetailIconRow(Icons.Filled.CalendarMonth, formatDateTime(state.dateTime!!))
                    DetailIconRow(Icons.Filled.Repeat, repeatDisplay(state.repeatType))
                    DetailIconRow(Icons.Filled.VolumeUp, soundDisplay(state.soundMode))
                }
            }

            state.category?.let { category ->
                AppCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CategoryDot(category.colorArgb)
                        Spacer(Modifier.padding(start = AppSpacing.sm))
                        Text(category.name, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            if (!state.isCompleted) {
                DetailAction(Icons.Filled.Done, stringResource(R.string.reminder_mark_complete)) {
                    viewModel.setCompleted(true)
                }
            }
            DetailAction(Icons.Filled.Edit, stringResource(R.string.edit), onEdit)
            DetailAction(Icons.Filled.Share, stringResource(R.string.settings_share)) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, state.title)
                }
                context.startActivity(Intent.createChooser(shareIntent, null))
            }
            DetailAction(Icons.Filled.Delete, stringResource(R.string.delete)) { showDeleteConfirm = true }
        }
    }

    if (showDeleteConfirm) {
        ConfirmationDialog(
            title = stringResource(R.string.reminder_delete_confirm_title),
            body = stringResource(R.string.reminder_delete_confirm_body),
            confirmLabel = stringResource(R.string.delete),
            isDestructive = true,
            onConfirm = {
                showDeleteConfirm = false
                viewModel.delete()
            },
            onDismiss = { showDeleteConfirm = false },
        )
    }
}

@Composable
private fun DetailIconRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
        Spacer(Modifier.padding(start = AppSpacing.sm))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun DetailAction(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = label)
        }
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun repeatDisplay(type: RepeatType): String = when (type) {
    RepeatType.NONE -> stringResource(R.string.reminder_repeat_none)
    RepeatType.DAILY -> stringResource(R.string.reminder_repeat_daily)
    RepeatType.WEEKDAY -> stringResource(R.string.reminder_repeat_weekday)
    RepeatType.WEEKLY -> stringResource(R.string.reminder_repeat_weekly)
    RepeatType.MONTHLY -> stringResource(R.string.reminder_repeat_monthly)
    RepeatType.YEARLY -> stringResource(R.string.reminder_repeat_yearly)
    RepeatType.CUSTOM -> stringResource(R.string.reminder_repeat_custom)
}

@Composable
private fun soundDisplay(mode: SoundMode): String = when (mode) {
    SoundMode.RING_ONCE -> stringResource(R.string.reminder_sound_once)
    SoundMode.REPEAT_UNTIL_DISMISSED -> stringResource(R.string.reminder_sound_repeat)
    SoundMode.SILENT -> stringResource(R.string.reminder_sound_silent)
}

private fun formatDateTime(millis: Long): String {
    val target = Calendar.getInstance().apply { timeInMillis = millis }
    val now = Calendar.getInstance()
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val dayLabel = when {
        target.get(Calendar.YEAR) == now.get(Calendar.YEAR) && target.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) -> "Today"
        else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(target.time)
    }
    return "$dayLabel · ${timeFormat.format(target.time)}"
}
