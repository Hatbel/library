package com.example.library.beans.serverModels

import com.google.gson.annotations.Expose


data class LoginRegisterModel(
    @Expose(serialize = false, deserialize = true)
    val mail: String,
    val password: String
)