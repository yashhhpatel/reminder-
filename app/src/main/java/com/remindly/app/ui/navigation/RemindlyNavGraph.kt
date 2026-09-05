package com.remindly.app.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.remindly.app.ui.theme.AppAnimation
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.screens.detail.ReminderDetailScreen
import com.remindly.app.ui.screens.home.HomeScreen
import com.remindly.app.ui.screens.location.LocationPickerScreen
import com.remindly.app.ui.screens.newreminder.AddCategoryScreen
import com.remindly.app.ui.screens.newreminder.NewReminderScreen
import com.remindly.app.ui.screens.newreminder.ReminderEditorViewModel
import com.remindly.app.ui.screens.newreminder.RepeatOptionsScreen
import com.remindly.app.ui.screens.newreminder.SelectCategoryScreen
import com.remindly.app.ui.screens.onboarding.OnboardingScreen
import com.remindly.app.ui.screens.onboarding.OverlayExplainerScreen
import com.remindly.app.ui.screens.premium.PremiumScreen
import com.remindly.app.ui.screens.privacy.PrivacyPolicyScreen
import com.remindly.app.ui.screens.privacy.PrivacySettingsScreen
import com.remindly.app.ui.screens.search.SearchScreen
import com.remindly.app.ui.screens.settings.LanguageScreen
import com.remindly.app.ui.screens.settings.SettingsScreen

@Composable
fun RemindlyNavGraph(
    navController: NavHostController,
    factory: RemindlyViewModelFactory,
    startDestination: String,
    isPremium: Boolean,
    onOpenPrivacyPolicyExternal: () -> Unit,
    onOpenFeedback: () -> Unit,
    onShare: () -> Unit,
    onRateUs: () -> Unit,
    onOnboardingComplete: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(animationSpec = tween(AppAnimation.NORMAL)) { it / 4 } + fadeIn(tween(AppAnimation.NORMAL))
        },
        exitTransition = {
            fadeOut(tween(AppAnimation.FAST))
        },
        popEnterTransition = {
            fadeIn(tween(AppAnimation.NORMAL))
        },
        popExitTransition = {
            slideOutHorizontally(animationSpec = tween(AppAnimation.NORMAL)) { it / 4 } + fadeOut(tween(AppAnimation.NORMAL))
        },
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Routes.OVERLAY_EXPLAINER) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
                onOpenPrivacyPolicy = onOpenPrivacyPolicyExternal,
            )
        }

        composable(Routes.OVERLAY_EXPLAINER) {
            OverlayExplainerScreen(
                onContinue = {
                    onOnboardingComplete()
                    navController.navigate(Routes.HOME) {
                        // popUpTo(ONBOARDING) is a no-op here: ONBOARDING was already popped off
                        // the back stack by the earlier ONBOARDING -> OVERLAY_EXPLAINER transition,
                        // so it's no longer a valid pop target. That silently left OVERLAY_EXPLAINER
                        // underneath HOME, so pressing back on Home revealed "One last step!" again
                        // instead of exiting. Pop up to the screen that's actually on the stack.
                        popUpTo(Routes.OVERLAY_EXPLAINER) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                factory = factory,
                onAddReminder = { navController.navigate(Routes.editorGraph(0L)) },
                onOpenSearch = { navController.navigate(Routes.SEARCH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onOpenReminder = { id -> navController.navigate(Routes.reminderDetail(id)) },
            )
        }

        editorGraph(navController, factory, isPremium)

        composable(
            route = Routes.REMINDER_DETAIL,
            arguments = listOf(navArgument(Routes.ARG_REMINDER_ID) { type = NavType.LongType }),
        ) { backStackEntry ->
            val reminderId = backStackEntry.arguments?.getLong(Routes.ARG_REMINDER_ID) ?: 0L
            val detailViewModel: ReminderEditorViewModel = viewModel(factory = factory)
            ReminderDetailScreen(
                reminderId = reminderId,
                viewModel = detailViewModel,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.editorGraph(reminderId)) },
                onDeleted = { navController.popBackStack(Routes.HOME, inclusive = false) },
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                factory = factory,
                onBack = { navController.popBackStack() },
                onOpenReminder = { id -> navController.navigate(Routes.reminderDetail(id)) },
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                factory = factory,
                onBack = { navController.popBackStack() },
                onOpenLanguage = { navController.navigate(Routes.LANGUAGE) },
                onOpenPrivacyPolicy = { navController.navigate(Routes.PRIVACY_POLICY) },
                onOpenPrivacySettings = { navController.navigate(Routes.PRIVACY_SETTINGS) },
                onOpenPremium = { navController.navigate(Routes.PREMIUM) },
                onOpenFeedback = onOpenFeedback,
                onShare = onShare,
                onRateUs = onRateUs,
            )
        }

        composable(Routes.LANGUAGE) {
            LanguageScreen(factory = factory, onBack = { navController.popBackStack() })
        }

        composable(Routes.PRIVACY_POLICY) {
            PrivacyPolicyScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.PRIVACY_SETTINGS) {
            PrivacySettingsScreen(factory = factory, onBack = { navController.popBackStack() })
        }

        composable(Routes.PREMIUM) {
            PremiumScreen(
                factory = factory,
                onClose = { navController.popBackStack() },
                onOpenTerms = { navController.navigate(Routes.PRIVACY_POLICY) },
                onOpenPrivacy = { navController.navigate(Routes.PRIVACY_POLICY) },
            )
        }
    }
}

private fun NavGraphBuilder.editorGraph(
    navController: NavHostController,
    factory: RemindlyViewModelFactory,
    isPremium: Boolean,
) {
    navigation(
        startDestination = Routes.EDITOR_FORM,
        route = Routes.EDITOR_GRAPH,
        arguments = listOf(navArgument(Routes.ARG_REMINDER_ID) { type = NavType.LongType }),
    ) {
        composable(Routes.EDITOR_FORM) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Routes.EDITOR_GRAPH) }
            val reminderId = parentEntry.arguments?.getLong(Routes.ARG_REMINDER_ID) ?: 0L
            val editorViewModel: ReminderEditorViewModel = viewModel(parentEntry, factory = factory)

            NewReminderScreen(
                reminderId = reminderId,
                isPremium = isPremium,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
                onOpenRepeat = { navController.navigate(Routes.EDITOR_REPEAT) },
                onOpenCategory = { navController.navigate(Routes.EDITOR_CATEGORY) },
                onOpenLocationPicker = { navController.navigate(Routes.EDITOR_LOCATION) },
                onOpenPremium = { navController.navigate(Routes.PREMIUM) },
                viewModel = editorViewModel,
            )
        }

        composable(Routes.EDITOR_REPEAT) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Routes.EDITOR_GRAPH) }
            val editorViewModel: ReminderEditorViewModel = viewModel(parentEntry, factory = factory)
            RepeatOptionsScreen(viewModel = editorViewModel, onBack = { navController.popBackStack() })
        }

        composable(Routes.EDITOR_CATEGORY) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Routes.EDITOR_GRAPH) }
            val editorViewModel: ReminderEditorViewModel = viewModel(parentEntry, factory = factory)
            SelectCategoryScreen(
                factory = factory,
                reminderEditorViewModel = editorViewModel,
                onBack = { navController.popBackStack() },
                onAddCategory = { navController.navigate(Routes.EDITOR_ADD_CATEGORY) },
            )
        }

        composable(Routes.EDITOR_ADD_CATEGORY) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Routes.EDITOR_GRAPH) }
            val editorViewModel: ReminderEditorViewModel = viewModel(parentEntry, factory = factory)
            AddCategoryScreen(
                factory = factory,
                reminderEditorViewModel = editorViewModel,
                onBack = { navController.popBackStack(Routes.EDITOR_CATEGORY, inclusive = false) },
            )
        }

        composable(Routes.EDITOR_LOCATION) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Routes.EDITOR_GRAPH) }
            val editorViewModel: ReminderEditorViewModel = viewModel(parentEntry, factory = factory)
            LocationPickerScreen(reminderEditorViewModel = editorViewModel, onBack = { navController.popBackStack() })
        }
    }
}
