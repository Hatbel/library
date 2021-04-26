package com.example.library.server.retrofit

import com.example.library.modules.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        if (sessionManager.isUserIn()) {
            requestBuilder.addHeader("Authorization", "Bearer ${sessionManager.token}")
                .build()
        }
        return chain.proceed(requestBuilder.build())
    }
}