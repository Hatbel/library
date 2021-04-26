package com.example.library.beans.daoModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*

@Entity
data class DaoBook(
        @PrimaryKey
        var id: Int,
        var name: String,
        var owner_id: Int,
        var status: String,
        var dead_line: Date,
        var reader_user_id: Int,
        var isRead: Boolean = false
)