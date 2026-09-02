package com.remindly.app.ui.navigation

object Routes {
    const val ONBOARDING = "onboarding"
    const val OVERLAY_EXPLAINER = "overlay_explainer"
    const val HOME = "home"

    const val EDITOR_GRAPH = "editor_graph/{reminderId}"
    const val EDITOR_FORM = "editor_form"
    const val EDITOR_REPEAT = "editor_repeat"
    const val EDITOR_CATEGORY = "editor_category"
    const val EDITOR_ADD_CATEGORY = "editor_add_category"
    const val EDITOR_LOCATION = "editor_location"

    const val REMINDER_DETAIL = "reminder_detail/{reminderId}"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    const val LANGUAGE = "language"
    const val PRIVACY_POLICY = "privacy_policy"
    const val PRIVACY_SETTINGS = "privacy_settings"
    const val PREMIUM = "premium"

    fun editorGraph(reminderId: Long) = "editor_graph/$reminderId"
    fun reminderDetail(id: Long) = "reminder_detail/$id"

    const val ARG_REMINDER_ID = "reminderId"
}
