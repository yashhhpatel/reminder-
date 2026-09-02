package com.remindly.app.ui.screens.newreminder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.CategorySwatches

@Composable
fun AddCategoryScreen(
    factory: RemindlyViewModelFactory,
    reminderEditorViewModel: ReminderEditorViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    categoryListViewModel: CategoryListViewModel = viewModel(factory = factory),
) {
    var name by remember { mutableStateOf("") }
    var selectedColorIndex by remember { mutableIntStateOf(CategorySwatches.lastIndex) }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = stringResource(R.string.category_add),
            onBack = onBack,
            actions = {
                IconButton(
                    onClick = {
                        categoryListViewModel.createCategory(name, CategorySwatches[selectedColorIndex].toArgb()) { newId ->
                            reminderEditorViewModel.setCategory(
                                com.remindly.app.domain.model.Category(id = newId, name = name.trim(), colorArgb = CategorySwatches[selectedColorIndex].toArgb())
                            )
                            onBack()
                        }
                    },
                    enabled = name.isNotBlank(),
                ) {
                    Icon(Icons.Filled.Check, contentDescription = stringResource(R.string.save))
                }
            },
        )

        Column(modifier = Modifier.padding(AppSpacing.md)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.category_name_hint)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(),
            )

            Spacer(Modifier.padding(top = AppSpacing.lg))
            Text(stringResource(R.string.category_pick_color), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.padding(top = AppSpacing.sm))

            LazyVerticalGrid(columns = GridCells.Fixed(5)) {
                items(CategorySwatches.size) { index ->
                    ColorSwatch(
                        color = CategorySwatches[index],
                        selected = index == selectedColorIndex,
                        onClick = { selectedColorIndex = index },
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(color: Color, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(AppSpacing.xs)
            .size(44.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(if (selected) 44.dp else 36.dp)
                .clip(CircleShape)
                .then(if (selected) Modifier.border(2.dp, color, CircleShape) else Modifier)
                .padding(if (selected) 4.dp else 0.dp)
                .clip(CircleShape)
                .background(color)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}
