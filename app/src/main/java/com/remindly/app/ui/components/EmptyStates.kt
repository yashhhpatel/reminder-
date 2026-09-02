package com.remindly.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.remindly.app.R
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme

@Composable
fun EmptyIllustration(icon: ImageVector, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(140.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun EmptyReminderState(
    onAddReminder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        EmptyIllustration(Icons.Filled.Notifications)
        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = AppSpacing.lg))
        Text(
            stringResource(R.string.home_empty_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = AppSpacing.xs))
        Text(
            stringResource(R.string.home_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.extendedColors.textSecondary,
            textAlign = TextAlign.Center,
        )
        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = AppSpacing.lg))
        PrimaryButton(
            text = stringResource(R.string.home_add_reminder),
            onClick = onAddReminder,
            modifier = Modifier.fillMaxWidth(0.8f),
        )
    }
}

@Composable
fun NoSearchResultsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        EmptyIllustration(Icons.Filled.SearchOff)
        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = AppSpacing.lg))
        Text(stringResource(R.string.home_no_results_title), style = MaterialTheme.typography.titleLarge)
        Text(
            stringResource(R.string.home_no_results_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.extendedColors.textSecondary,
        )
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}
