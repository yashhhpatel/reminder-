package com.remindly.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.remindly.app.R
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.PillShape
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class QuickTimeChip(val label: String, val dateTimeMillis: Long?)

@Composable
fun BottomQuickAdd(
    onSubmit: (text: String, dateTimeMillis: Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var selectedChip by remember { mutableStateOf<QuickTimeChip?>(null) }

    val chips = remember { buildQuickTimeChips() }

    Column(modifier = modifier.fillMaxWidth()) {
        if (isFocused) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
                contentPadding = PaddingValues(horizontal = AppSpacing.md, vertical = AppSpacing.xs),
            ) {
                items(chips) { chip ->
                    QuickSuggestionChip(
                        label = chip.label,
                        onClick = { selectedChip = chip },
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                placeholder = { Text(stringResource(R.string.home_quick_add_hint)) },
                singleLine = true,
                shape = PillShape,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                colors = OutlinedTextFieldDefaults.colors(),
            )
            Spacer(Modifier.width(AppSpacing.sm))
            CircularIconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onSubmit(text.trim(), selectedChip?.dateTimeMillis)
                        text = ""
                        selectedChip = null
                    }
                },
            ) {
                Icon(
                    imageVector = if (isFocused || text.isNotBlank()) Icons.Filled.Check else Icons.Filled.Add,
                    contentDescription = stringResource(R.string.home_add_reminder),
                    tint = Color.White,
                )
            }
        }
    }
}

private fun buildQuickTimeChips(): List<QuickTimeChip> {
    val now = Calendar.getInstance()
    val laterToday = (now.clone() as Calendar).apply { add(Calendar.HOUR_OF_DAY, 3) }
    val thisEvening = (now.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 19)
        set(Calendar.MINUTE, 0)
    }
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    return listOf(
        QuickTimeChip("Later today", laterToday.timeInMillis),
        QuickTimeChip("This evening", thisEvening.timeInMillis),
        QuickTimeChip("No time", null),
        QuickTimeChip("Today ${timeFormat.format(now.time)}", now.timeInMillis),
    )
}
