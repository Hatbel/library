package com.example.library.modules

import android.content.Context
import androidx.annotation.StringRes

class StringHelper(private val context: Context) {
    fun getStringFromRes(@StringRes stringId: Int) = context.resources.getString(stringId)
}