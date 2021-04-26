package com.example.library.interfaces.daoInterfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.library.beans.daoModels.DaoBook
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface BookDao {
    @Query("SELECT * FROM DaoBook where id = :id")
    suspend fun getBookById(id: Int): DaoBook

    @Query("SELECT * FROM DaoBook where isRead = :isRead")
    suspend fun getReadBooks(isRead: Boolean): List<DaoBook>

    @Query("SELECT * FROM DaoBook where status = :status")
    fun getBooksByStatus(status: String): Flow<List<DaoBook>>

    @Query("SELECT * FROM DaoBook where dead_line < :date")
    fun getBooksByDate(date: Date): Flow<List<DaoBook>>

    @Query("SELECT * FROM DaoBook where owner_id = :id")
    suspend fun getBooksByUserId(id : Int): List<DaoBook>

    @Query("DELETE FROM  DaoBook")
    suspend fun clearBooksTable()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllBooks(books: List<DaoBook>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: DaoBook)

    @Query("SELECT * FROM DaoBook")
    fun getBooks(): Flow<List<DaoBook>>
}