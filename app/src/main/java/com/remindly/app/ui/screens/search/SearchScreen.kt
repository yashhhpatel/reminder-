package com.remindly.app.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.AppSearchBar
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.components.NoSearchResultsState
import com.remindly.app.ui.components.ReminderCard
import com.remindly.app.ui.theme.AppSpacing

@Composable
fun SearchScreen(
    factory: RemindlyViewModelFactory,
    onBack: () -> Unit,
    onOpenReminder: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = factory),
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()
    val categoriesById by viewModel.categoriesById.collectAsState()
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.search_title), onBack = onBack)
        AppSearchBar(
            query = query,
            onQueryChange = viewModel::onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md),
            focusRequester = focusRequester,
        )
        Spacer(Modifier.height(AppSpacing.sm))

        if (query.isNotBlank() && results.isEmpty()) {
            NoSearchResultsState(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(contentPadding = PaddingValues(horizontal = AppSpacing.md, vertical = AppSpacing.xs)) {
                items(results, key = { it.id }) { reminder ->
                    ReminderCard(
                        reminder = reminder,
                        subtitle = reminder.placeName ?: "",
                        categoryColor = categoriesById[reminder.categoryId]?.colorArgb ?: 0xFF6A3DE8.toInt(),
                        onClick = { onOpenReminder(reminder.id) },
                        onToggleComplete = {},
                        modifier = Modifier.padding(bottom = AppSpacing.xs),
                    )
                }
            }
        }
    }
}
