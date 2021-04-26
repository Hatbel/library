package com.example.library.beans.serverModels

import com.google.gson.annotations.Expose

data class CreateBookModel(
    @Expose(serialize = false, deserialize = true)
    val name: String
)