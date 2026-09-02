package com.remindly.app.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.remindly.app.ui.components.EmptyReminderState
import com.remindly.app.ui.theme.RemindlyTheme
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented UI test — requires a connected device or emulator to run
 * (./gradlew connectedDebugAndroidTest).
 */
class HomeEmptyStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyState_showsWelcomeHeadlineAndAddButton() {
        composeTestRule.setContent {
            RemindlyTheme {
                EmptyReminderState(onAddReminder = {})
            }
        }

        composeTestRule.onNodeWithText("Welcome to Reminders").assertExists()
        composeTestRule.onNodeWithText("Add reminder").assertExists()
    }
}
