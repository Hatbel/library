package com.example.library.beans.serverModels

import com.google.gson.annotations.Expose


data class LoginRegisterModel(
    @Expose(serialize = false, deserialize = true)
    var mail: String,
    var password: String
)