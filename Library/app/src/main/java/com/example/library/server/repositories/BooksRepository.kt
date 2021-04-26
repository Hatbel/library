package com.example.library.server.repositories

import android.util.Log
import com.example.library.beans.daoModels.DaoBook
import com.example.library.beans.serverModels.BooksData
import com.example.library.beans.serverModels.CreateBookModel
import com.example.library.beans.serverModels.IN_LIBRARY
import com.example.library.beans.serverModels.NO_DEADLINE
import com.example.library.interfaces.apiInterfaces.BookApi
import com.example.library.interfaces.daoInterfaces.BookDao
import com.example.library.modules.SessionManager
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class BooksRepository(
    private val bookApi: BookApi,
    private val sessionManager: SessionManager,
    private val bookDao: BookDao
) {

    fun observeBooks() = bookDao.getBooks()

    suspend fun loadBooks(limit: Int, page: Int) {
        try {
            val books = bookApi.getListOfBooks(limit, page).data
            fullDeadlines(books)
            bookDao.insertAllBooks(books.map { it.toDaoBook() })
        } catch (e: Exception) {
            Log.e("no internet", e.message.toString())
        }
    }

    suspend fun getOwnBooks(): List<DaoBook> {
        return try {
            val books = bookApi.getOwnBooks().data
            fullDeadlines(books)
            books.map { it.toDaoBook() }
        } catch (e: java.lang.Exception) {
            bookDao.getBooksByUserId(sessionManager.userId)
        }
    }

    suspend fun getBook(): DaoBook {
        return try {
            bookApi.getBook(sessionManager.bookId).data.toDaoBook()
        } catch (e: Exception) {
            bookDao.getBookById(sessionManager.bookId)

        }
    }

    suspend fun reserveBook(): DaoBook {
        val book = bookApi.reserveBook(sessionManager.bookId)
        book.data.isRead = true
        bookDao.insertBook(book.data.toDaoBook())
        return book.data.toDaoBook()
    }

    suspend fun returnBook() {
        bookApi.returnBook(sessionManager.bookId)
    }

    suspend fun getUserReadBooks(): DaoBook =
        bookApi.getUserReadBook().data.toDaoBook()

    suspend fun createBook(name: String): DaoBook =
        bookApi.createBook(CreateBookModel(name)).data.toDaoBook()

    suspend fun returnBookToOwner(): DaoBook =
        bookApi.returnBookToOwner(sessionManager.bookId).data.toDaoBook()

    suspend fun getReadBooks(): List<DaoBook> {
        return bookDao.getReadBooks(true)
    }

    fun observeAvailableBooks() = bookDao.getBooksByStatus(IN_LIBRARY)

    suspend fun loadAvailableBooks(limit: Int, page: Int) {
        try {
            val books = bookApi.getAvailableBooks(limit, page).data
            fullDeadlines(books)
            bookDao.insertAllBooks(books.map { it.toDaoBook() })
        } catch (e: Exception) {
            Log.e("no internet", e.message.toString())
        }
    }

    fun observeExpiredBooks(): Flow<List<DaoBook>> {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        return bookDao.getBooksByDate(sdf.parse(sdf.format(Date())))
    }

    suspend fun loadExpiredBooks(limit: Int, page: Int) {
        try {
            val books = bookApi.getExpiredBooks(limit, page).data
            fullDeadlines(books)
            bookDao.insertAllBooks(books.map { it.toDaoBook() })
        } catch (e: Exception) {
            Log.e("no internet", e.message.toString())
        }
    }

    private fun fullDeadlines(books: List<BooksData>) {
        for (book in books) {
            if (book.dead_line.isNullOrBlank()) {
                book.dead_line = NO_DEADLINE
            }
        }
    }

    suspend fun clearTables() {
        bookDao.clearBooksTable()
    }
}

fun String.toDate(): Date {
    val format = SimpleDateFormat(DATE_FORMAT, Locale.US)
    return format.parse(this)
}

fun BooksData.toDaoBook(): DaoBook =
    DaoBook(
        id,
        name,
        owner_id,
        status,
        if (dead_line != NO_DEADLINE) dead_line.toDate() else Date(),
        reader_user_id
    )

fun Date.toyyyymmddString(): String = SimpleDateFormat(DATE_FORMAT, Locale.US).format(this)

const val DATE_FORMAT = "yyyy-mm-dd"