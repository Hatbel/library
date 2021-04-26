package com.example.library.modules

import android.content.Intent
import com.example.library.R


class DeepLinkManager(private val sessionManager: SessionManager) {

    fun manageLink(appLinkIntent: Intent): Int {
        return if (sessionManager.isUserIn() && appLinkIntent.data != null) {
            when {
                appLinkIntent.data?.pathSegments?.get(1)?.contains("createBook") as Boolean -> {
                    R.id.createBookFragment
                }
                appLinkIntent.data?.pathSegments?.get(1)
                    ?.contains("bookId/[0-9]+".toRegex()) as Boolean -> {
                    sessionManager.bookId = appLinkIntent.data?.lastPathSegment?.toInt() as Int
                    R.id.showBookFragment
                }
                else -> R.id.fragmentBooks
            }
        } else -1
    }

}