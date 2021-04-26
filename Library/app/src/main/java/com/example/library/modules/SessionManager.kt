package com.example.library.modules

import android.content.Context
import androidx.core.content.edit

const val USER_TOKEN = "user_token"
const val USER_ID = "user_id"
const val PAGE = "page"
const val BOOK_ID = "book_id"

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var userId: Int
        get() = prefs.getInt(USER_ID, 0)
        set(value) = prefs.edit {
            putInt(USER_ID, value)
        }
    var page: Int
        get() = prefs.getInt(PAGE, 1)
        set(value) = prefs.edit {
            putInt(PAGE, value)
        }
    var bookId: Int
        get() = prefs.getInt(BOOK_ID, 0)
        set(value) = prefs.edit {
            putInt(BOOK_ID, value)
        }

    var token: String
        get() = prefs.getString(USER_TOKEN, "") ?: ""
        set(value) = prefs.edit {
            putString(USER_TOKEN, value)
        }

    fun clearPrefs() {
        prefs.edit().remove(USER_ID).apply()
        prefs.edit().remove(USER_TOKEN).apply()
    }

    fun isUserIn() = token.isNotBlank()
}