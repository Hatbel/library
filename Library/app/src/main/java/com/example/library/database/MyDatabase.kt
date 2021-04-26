package com.example.library.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.library.beans.daoModels.DaoBook
import com.example.library.beans.daoModels.DaoUser
import com.example.library.interfaces.daoInterfaces.BookDao
import com.example.library.interfaces.daoInterfaces.UserDao

@Database(entities = [DaoUser::class, DaoBook::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
}