package com.example.library.beans.serverModels

import com.google.gson.annotations.Expose

data class ApiResponse<T>(
    @Expose(serialize = false, deserialize = true)
    var code: Int,
    var status: String,
    var data: T
)

data class UserData(
    @Expose(serialize = false, deserialize = true)
    var id: Int,
    var mail: String,
    var token: String
)

data class BooksData(
    @Expose(serialize = false, deserialize = true)
    var id: Int,
    var name: String,
    var owner_id: Int,
    var status: String,
    var dead_line: String,
    var reader_user_id: Int,
    @Expose(serialize = false, deserialize = false)
    var isRead: Boolean = false
)

const val IN_LIBRARY = "in_library"
const val PICKED_UP = "picked_up"
const val NO_DEADLINE = "noDeadLine"