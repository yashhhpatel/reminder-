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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
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

    val laterTodayLabel = stringResource(R.string.quick_chip_later_today)
    val thisEveningLabel = stringResource(R.string.quick_chip_this_evening)
    val noTimeLabel = stringResource(R.string.quick_chip_no_time)
    val currentTimeChipFormat = stringResource(R.string.home_current_time_chip)
    val chips = remember { buildQuickTimeChips(laterTodayLabel, thisEveningLabel, noTimeLabel, currentTimeChipFormat) }

    val exampleRes = listOf(
        R.string.home_quick_add_example_1,
        R.string.home_quick_add_example_2,
        R.string.home_quick_add_example_3,
        R.string.home_quick_add_example_4,
    )
    var exampleIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(isFocused) {
        if (!isFocused) {
            while (true) {
                kotlinx.coroutines.delay(3000)
                exampleIndex = (exampleIndex + 1) % exampleRes.size
            }
        }
    }
    val placeholderText = if (isFocused || text.isNotEmpty()) {
        stringResource(R.string.home_quick_add_hint)
    } else {
        stringResource(exampleRes[exampleIndex])
    }

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
                placeholder = { Text(placeholderText) },
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

private fun buildQuickTimeChips(
    laterTodayLabel: String,
    thisEveningLabel: String,
    noTimeLabel: String,
    currentTimeChipFormat: String,
): List<QuickTimeChip> {
    val now = Calendar.getInstance()
    val laterToday = (now.clone() as Calendar).apply { add(Calendar.HOUR_OF_DAY, 3) }
    val thisEvening = (now.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 19)
        set(Calendar.MINUTE, 0)
    }
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    return listOf(
        QuickTimeChip(laterTodayLabel, laterToday.timeInMillis),
        QuickTimeChip(thisEveningLabel, thisEvening.timeInMillis),
        QuickTimeChip(noTimeLabel, null),
        QuickTimeChip(String.format(currentTimeChipFormat, timeFormat.format(now.time)), now.timeInMillis),
    )
}
