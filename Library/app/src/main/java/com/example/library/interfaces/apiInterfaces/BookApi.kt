package com.example.library.interfaces.apiInterfaces

import com.example.library.beans.serverModels.ApiResponse
import com.example.library.beans.serverModels.BooksData
import com.example.library.beans.serverModels.CreateBookModel
import retrofit2.http.*

interface BookApi {
    @GET("books")
    suspend fun getListOfBooks(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<List<BooksData>>

    @POST("books")
    suspend fun createBook(@Body book: CreateBookModel): ApiResponse<BooksData>

    @GET("books/{id}")
    suspend fun getBook(@Path("id") id: Int): ApiResponse<BooksData>

    @PUT("books/reserve/{id}")
    suspend fun reserveBook(@Path("id") id: Int): ApiResponse<BooksData>

    @PUT("books/return/{id}")
    suspend fun returnBook(@Path("id") id: Int)

    @GET("book/user_read")
    suspend fun getUserReadBook(): ApiResponse<BooksData>

    @GET("book/own_books")
    suspend fun getOwnBooks(): ApiResponse<List<BooksData>>

    @GET("book/available_books")
    suspend fun getAvailableBooks(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<List<BooksData>>

    @PUT("book/return_to_owner/{id}")
    suspend fun returnBookToOwner(@Path("id") id: Int): ApiResponse<BooksData>

    @GET("book/expired")
    suspend fun getExpiredBooks(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<List<BooksData>>
}