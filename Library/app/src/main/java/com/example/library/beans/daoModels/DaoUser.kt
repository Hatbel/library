package com.example.library.beans.daoModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity
data class DaoUser(
    @PrimaryKey
    var id: Int,
    var mail: String
)