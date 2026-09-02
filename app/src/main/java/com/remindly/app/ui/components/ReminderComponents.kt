package com.remindly.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.remindly.app.domain.model.Reminder
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme
import com.remindly.app.ui.theme.PillShape

@Composable
fun CategoryDot(colorArgb: Int, modifier: Modifier = Modifier, size: androidx.compose.ui.unit.Dp = 12.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(colorArgb)),
    )
}

@Composable
fun ReminderCard(
    reminder: Reminder,
    subtitle: String,
    categoryColor: Int,
    onClick: () -> Unit,
    onToggleComplete: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
                    .background(if (reminder.isCompleted) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { onToggleComplete(!reminder.isCompleted) },
                contentAlignment = Alignment.Center,
            ) {
                if (reminder.isCompleted) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(Modifier.width(AppSpacing.sm))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    reminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else null,
                    color = if (reminder.isCompleted) AppTheme.extendedColors.textSecondary else MaterialTheme.colorScheme.onSurface,
                )
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = AppTheme.extendedColors.textSecondary)
            }
            Spacer(Modifier.width(AppSpacing.xs))
            CategoryDot(categoryColor)
        }
    }
}

@Composable
fun QuickSuggestionChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Filled.NotificationsActive,
) {
    Row(
        modifier = modifier
            .clip(PillShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(AppSpacing.xxs))
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}
