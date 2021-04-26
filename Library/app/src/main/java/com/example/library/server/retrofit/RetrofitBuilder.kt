package com.example.library.server.retrofit


import com.example.library.interfaces.apiInterfaces.BookApi
import com.example.library.interfaces.apiInterfaces.UserApi
import com.example.library.modules.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder(private val sessionManager: SessionManager) {
    private fun getRetrofit(): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor(TokenInterceptor(sessionManager)).build()
        return Retrofit.Builder()
            .baseUrl("https://powerful-citadel-31931.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).client(client)
            .build()
    }

    fun getBookApiService() = getRetrofit().create(BookApi::class.java)
    fun getUserApiService() = getRetrofit().create(UserApi::class.java)
}