package com.remindly.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme

@Composable
fun SettingsSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = AppTheme.extendedColors.textSecondary,
        modifier = modifier.padding(start = AppSpacing.xs, bottom = AppSpacing.xxs),
    )
}

@Composable
fun SettingsRow(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    subtitle: String? = null,
    valueText: String? = null,
    premium: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    AppCard(
        modifier = modifier,
        premiumBorder = premium,
        onClick = onClick,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.width(AppSpacing.sm))
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    if (premium) {
                        Spacer(Modifier.width(AppSpacing.xxs))
                        PremiumBadge(modifier = Modifier.size(16.dp))
                    }
                }
                if (valueText != null) {
                    Text(valueText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = AppTheme.extendedColors.textSecondary)
                }
            }
            trailing?.invoke(this)
        }
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    subtitle: String? = null,
    premium: Boolean = false,
    enabled: Boolean = true,
) {
    SettingsRow(
        title = title,
        modifier = modifier,
        icon = icon,
        subtitle = subtitle,
        premium = premium,
        trailing = {
            AppSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                contentDescription = title,
            )
        },
    )
}

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
) {
    val extended = AppTheme.extendedColors
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        modifier = modifier.semantics {
            if (contentDescription != null) this.contentDescription = contentDescription
        },
        colors = SwitchDefaults.colors(
            checkedTrackColor = extended.toggleOnTrack,
            checkedThumbColor = extended.toggleKnobOn,
            uncheckedTrackColor = extended.toggleOffTrack,
            uncheckedThumbColor = extended.toggleKnobOff,
            uncheckedBorderColor = extended.toggleOffTrack,
        ),
    )
}
