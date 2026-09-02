package com.remindly.app.ui.screens.newreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindly.app.domain.model.Category
import com.remindly.app.domain.model.Reminder
import com.remindly.app.domain.model.RepeatType
import com.remindly.app.domain.model.SoundMode
import com.remindly.app.domain.repository.CategoryRepository
import com.remindly.app.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class ReminderEditorState(
    val id: Long = 0L,
    val title: String = "",
    val description: String? = null,
    val dateTime: Long? = null,
    val addTimeEnabled: Boolean = false,
    val repeatType: RepeatType = RepeatType.NONE,
    val repeatDays: Set<Int> = emptySet(),
    val soundMode: SoundMode = SoundMode.RING_ONCE,
    val placeEnabled: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val placeName: String? = null,
    val categoryId: Long = Category.DEFAULT_CATEGORY_ID,
    val category: Category? = null,
    val isCompleted: Boolean = false,
    val isLoaded: Boolean = false,
    val isEditMode: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
) {
    val isValid: Boolean get() = title.isNotBlank()
}

class ReminderEditorViewModel(
    private val reminderRepository: ReminderRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ReminderEditorState())
    val state: StateFlow<ReminderEditorState> = _state.asStateFlow()

    fun load(reminderId: Long?) {
        if (reminderId == null || reminderId == 0L) {
            _state.value = ReminderEditorState(isLoaded = true, isEditMode = false)
            loadCategory(Category.DEFAULT_CATEGORY_ID)
            return
        }
        viewModelScope.launch {
            val reminder = reminderRepository.getById(reminderId)
            if (reminder != null) {
                _state.value = reminder.toEditorState()
                loadCategory(reminder.categoryId)
            } else {
                _state.value = ReminderEditorState(isLoaded = true, isEditMode = false)
            }
        }
    }

    private fun loadCategory(categoryId: Long) {
        viewModelScope.launch {
            val category = categoryRepository.getById(categoryId)
            _state.value = _state.value.copy(category = category)
        }
    }

    fun updateTitle(text: String) {
        _state.value = _state.value.copy(title = text)
    }

    fun toggleAddTime(enabled: Boolean) {
        _state.value = if (enabled) {
            val defaultTime = Calendar.getInstance().apply {
                add(Calendar.HOUR_OF_DAY, 1)
            }.timeInMillis
            _state.value.copy(addTimeEnabled = true, dateTime = _state.value.dateTime ?: defaultTime)
        } else {
            _state.value.copy(addTimeEnabled = false, dateTime = null)
        }
    }

    fun setDateTime(millis: Long) {
        _state.value = _state.value.copy(dateTime = millis, addTimeEnabled = true)
    }

    fun clearDateTime() {
        _state.value = _state.value.copy(dateTime = null, addTimeEnabled = false)
    }

    fun setRepeat(type: RepeatType, days: Set<Int> = emptySet()) {
        _state.value = _state.value.copy(repeatType = type, repeatDays = days)
    }

    fun setSound(mode: SoundMode) {
        _state.value = _state.value.copy(soundMode = mode)
    }

    fun togglePlace(enabled: Boolean) {
        _state.value = _state.value.copy(placeEnabled = enabled)
    }

    fun setPlace(latitude: Double, longitude: Double, name: String) {
        _state.value = _state.value.copy(
            placeEnabled = true,
            latitude = latitude,
            longitude = longitude,
            placeName = name,
        )
    }

    fun clearPlace() {
        _state.value = _state.value.copy(placeEnabled = false, latitude = null, longitude = null, placeName = null)
    }

    fun setCategory(category: Category) {
        _state.value = _state.value.copy(categoryId = category.id, category = category)
    }

    fun save() {
        val current = _state.value
        if (!current.isValid) return
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            reminderRepository.save(
                Reminder(
                    id = current.id,
                    title = current.title.trim(),
                    description = current.description,
                    createdAt = now,
                    updatedAt = now,
                    dateTime = current.dateTime,
                    repeatType = current.repeatType,
                    repeatDays = current.repeatDays,
                    notificationEnabled = current.addTimeEnabled,
                    soundMode = current.soundMode,
                    placeEnabled = current.placeEnabled,
                    latitude = current.latitude,
                    longitude = current.longitude,
                    placeName = current.placeName,
                    categoryId = current.categoryId,
                    isCompleted = current.isCompleted,
                )
            )
            _state.value = _state.value.copy(isSaved = true)
        }
    }

    fun delete() {
        val current = _state.value
        if (current.id == 0L) return
        viewModelScope.launch {
            reminderRepository.getById(current.id)?.let { reminderRepository.delete(it) }
            _state.value = _state.value.copy(isDeleted = true)
        }
    }

    fun setCompleted(completed: Boolean) {
        val current = _state.value
        _state.value = current.copy(isCompleted = completed)
        if (current.id != 0L) {
            viewModelScope.launch { reminderRepository.setCompleted(current.id, completed) }
        }
    }

    private fun Reminder.toEditorState() = ReminderEditorState(
        id = id,
        title = title,
        description = description,
        dateTime = dateTime,
        addTimeEnabled = dateTime != null && notificationEnabled,
        repeatType = repeatType,
        repeatDays = repeatDays,
        soundMode = soundMode,
        placeEnabled = placeEnabled,
        latitude = latitude,
        longitude = longitude,
        placeName = placeName,
        categoryId = categoryId,
        isCompleted = isCompleted,
        isLoaded = true,
        isEditMode = true,
    )
}
