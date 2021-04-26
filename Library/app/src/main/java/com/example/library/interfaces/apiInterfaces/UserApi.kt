package com.example.library.interfaces.apiInterfaces

import com.example.library.beans.serverModels.ApiResponse
import com.example.library.beans.serverModels.LoginRegisterModel
import com.example.library.beans.serverModels.UserData
import retrofit2.http.*

interface UserApi {
    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<List<UserData>>

    @POST("users")
    suspend fun createUser(@Body user: LoginRegisterModel): ApiResponse<UserData>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): ApiResponse<UserData>

    @POST("login")
    suspend fun login(@Body user: LoginRegisterModel): ApiResponse<UserData>
}