package com.remindly.app.ui.screens.newreminder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.domain.model.Category
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.AppCard
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.components.ConfirmationDialog
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme

@Composable
fun SelectCategoryScreen(
    factory: RemindlyViewModelFactory,
    reminderEditorViewModel: ReminderEditorViewModel,
    onBack: () -> Unit,
    onAddCategory: () -> Unit,
    modifier: Modifier = Modifier,
    categoryListViewModel: CategoryListViewModel = viewModel(factory = factory),
) {
    val categories by categoryListViewModel.categories.collectAsState()
    val countsByCategory by categoryListViewModel.reminderCountByCategory.collectAsState()
    val editorState by reminderEditorViewModel.state.collectAsState()
    var categoryPendingDelete by remember { mutableStateOf<Category?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.select_category_title), onBack = onBack)

        LazyColumn(contentPadding = PaddingValues(AppSpacing.md)) {
            items(categories, key = { it.id }) { category ->
                CategoryRow(
                    category = category,
                    count = countsByCategory[category.id] ?: 0,
                    selected = editorState.categoryId == category.id,
                    onSelect = {
                        reminderEditorViewModel.setCategory(category)
                        onBack()
                    },
                    onDelete = if (!category.isBuiltIn) {
                        { categoryPendingDelete = category }
                    } else null,
                )
                Spacer(Modifier.padding(top = AppSpacing.xs))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onAddCategory)
                        .padding(vertical = AppSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(AppSpacing.sm))
                    Text(stringResource(R.string.category_add), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }

    categoryPendingDelete?.let { category ->
        ConfirmationDialog(
            title = stringResource(R.string.delete),
            body = stringResource(R.string.category_delete_confirm),
            confirmLabel = stringResource(R.string.delete),
            isDestructive = true,
            onConfirm = {
                if (editorState.categoryId == category.id) {
                    categories.firstOrNull { it.id == Category.DEFAULT_CATEGORY_ID }
                        ?.let { reminderEditorViewModel.setCategory(it) }
                }
                categoryListViewModel.deleteCategory(category)
                categoryPendingDelete = null
            },
            onDismiss = { categoryPendingDelete = null },
        )
    }
}

@Composable
private fun CategoryRow(
    category: Category,
    count: Int,
    selected: Boolean,
    onSelect: () -> Unit,
    onDelete: (() -> Unit)?,
) {
    AppCard(onClick = onSelect) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .then(
                        if (selected) {
                            Modifier.background(Color(category.colorArgb))
                        } else {
                            Modifier.border(2.dp, Color(category.colorArgb), CircleShape)
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (selected) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(Modifier.width(AppSpacing.sm))
            Text(category.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Text(
                count.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = AppTheme.extendedColors.textSecondary,
            )
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete), tint = AppTheme.extendedColors.textSecondary)
                }
            }
        }
    }
}
