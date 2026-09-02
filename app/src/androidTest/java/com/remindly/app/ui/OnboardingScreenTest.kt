package com.remindly.app.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.remindly.app.ui.screens.onboarding.OnboardingScreen
import com.remindly.app.ui.theme.RemindlyTheme
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented UI test — requires a connected device or emulator to run
 * (./gradlew connectedDebugAndroidTest).
 */
class OnboardingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tappingStartNow_showsNotificationExplanationDialog() {
        composeTestRule.setContent {
            RemindlyTheme {
                OnboardingScreen(onFinished = {}, onOpenPrivacyPolicy = {})
            }
        }

        composeTestRule.onNodeWithText("START NOW").performClick()
        composeTestRule.onNodeWithText("Notification Required").assertExists()
    }
}
