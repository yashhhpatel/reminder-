package com.remindly.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.remindly.app.R
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.PillShape
import java.util.Calendar

@Composable
fun BottomQuickAdd(
    onSubmit: (text: String, dateTimeMillis: Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pendingDateTime by remember { mutableStateOf(0L) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun submit(dateTimeMillis: Long?) {
        if (text.isNotBlank()) {
            onSubmit(text.trim(), dateTimeMillis)
            text = ""
        }
    }

    fun startAddFlow() {
        if (text.isBlank()) return
        keyboardController?.hide()
        focusManager.clearFocus()
        pendingDateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }.timeInMillis
        showDatePicker = true
    }

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
                keyboardActions = KeyboardActions(onDone = { startAddFlow() }),
                colors = OutlinedTextFieldDefaults.colors(),
            )
            Spacer(Modifier.width(AppSpacing.sm))
            CircularIconButton(
                onClick = { startAddFlow() },
            ) {
                Icon(
                    imageVector = if (isFocused || text.isNotBlank()) Icons.Filled.Check else Icons.Filled.Add,
                    contentDescription = stringResource(R.string.home_add_reminder),
                    tint = Color.White,
                )
            }
        }
    }

    if (showDatePicker) {
        AppDatePickerDialog(
            initialMillis = pendingDateTime,
            onConfirm = {
                pendingDateTime = mergeDateKeepTime(it, pendingDateTime)
                showDatePicker = false
                showTimePicker = true
            },
            onDismiss = { showDatePicker = false },
        )
    }
    if (showTimePicker) {
        val cal = Calendar.getInstance().apply { timeInMillis = pendingDateTime }
        AppTimePickerDialog(
            initialHour = cal.get(Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(Calendar.MINUTE),
            is24Hour = android.text.format.DateFormat.is24HourFormat(context),
            onConfirm = { hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                showTimePicker = false
                submit(cal.timeInMillis)
            },
            onDismiss = { showTimePicker = false },
        )
    }
}

private fun mergeDateKeepTime(newDateMillis: Long, oldMillis: Long): Long {
    val newCal = Calendar.getInstance().apply { timeInMillis = newDateMillis }
    val oldCal = Calendar.getInstance().apply { timeInMillis = oldMillis }
    newCal.set(Calendar.HOUR_OF_DAY, oldCal.get(Calendar.HOUR_OF_DAY))
    newCal.set(Calendar.MINUTE, oldCal.get(Calendar.MINUTE))
    return newCal.timeInMillis
}
