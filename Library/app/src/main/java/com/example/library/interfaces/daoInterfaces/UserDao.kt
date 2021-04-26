package com.example.library.interfaces.daoInterfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.library.beans.daoModels.DaoUser
import com.example.library.beans.serverModels.UserData

@Dao
interface UserDao {
    @Query("SELECT * FROM DaoUser where id = :id")
    suspend fun getUserById(id: Int): DaoUser

    @Query("DELETE FROM  DaoUser")
    suspend fun clearUserTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: DaoUser)

}