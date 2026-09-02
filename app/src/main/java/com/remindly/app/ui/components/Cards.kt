package com.remindly.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.remindly.app.R
import com.remindly.app.ui.theme.AppDimens
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme
import com.remindly.app.ui.theme.PillShape

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    premiumBorder: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val extended = AppTheme.extendedColors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimens.cardCornerRadius))
            .background(MaterialTheme.colorScheme.surface)
            .then(
                if (premiumBorder) {
                    Modifier.border(
                        AppDimens.borderWidth,
                        extended.premiumGoldBorder,
                        RoundedCornerShape(AppDimens.cardCornerRadius),
                    )
                } else Modifier
            )
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(AppSpacing.md),
        content = content,
    )
}

@Composable
fun PremiumBadge(modifier: Modifier = Modifier, size: Dp = AppDimens.premiumBadgeSize) {
    val extended = AppTheme.extendedColors
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(extended.premiumGold)
            .padding(2.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(1.dp),
        )
    }
}

@Composable
fun GoProBanner(
    onGoPro: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = AppTheme.extendedColors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimens.cardCornerRadius))
            .background(
                Brush.horizontalGradient(listOf(extended.goProGradientStart, extended.goProGradientEnd))
            )
            .clickable(onClick = onGoPro)
            .padding(AppSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("👑", style = MaterialTheme.typography.headlineMedium)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = AppSpacing.sm),
        ) {
            Text(
                stringResource(R.string.settings_premium_title),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                stringResource(R.string.settings_premium_subtitle),
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Box(
            modifier = Modifier
                .clip(PillShape)
                .background(Color.White)
                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs),
        ) {
            Text(
                stringResource(R.string.settings_go_pro),
                color = extended.goProGradientEnd,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
