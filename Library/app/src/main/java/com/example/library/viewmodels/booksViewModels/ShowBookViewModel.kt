package com.example.library.viewmodels.booksViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.modules.SessionManager
import com.example.library.server.BookScreenState
import com.example.library.server.repositories.BooksRepository
import kotlinx.coroutines.launch

class ShowBookViewModel(
    private val sessionManager: SessionManager,
    private val bookApiRepository: BooksRepository
) : ViewModel() {
    private val _state = MutableLiveData<BookScreenState>()
    val state: LiveData<BookScreenState>
        get() = _state

    init {
        getListOfBooks()
    }

    private fun getListOfBooks() {
        viewModelScope.launch {
            val book = bookApiRepository.getBook()
            _state.value = try {
                sessionManager.bookId = book.id
                BookScreenState.Book(book)
            } catch (e: Exception) {
                BookScreenState.Error(e.localizedMessage)
            }
        }
    }
}