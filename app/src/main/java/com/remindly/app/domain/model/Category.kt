package com.remindly.app.domain.model

data class Category(
    val id: Long = 0L,
    val name: String,
    val colorArgb: Int,
    val isBuiltIn: Boolean = false,
    val sortOrder: Int = 0,
) {
    companion object {
        const val DEFAULT_CATEGORY_ID = 1L
    }
}
