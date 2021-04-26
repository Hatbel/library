package com.example.library.viewmodels.booksViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.modules.SessionManager
import com.example.library.server.repositories.BooksRepository
import com.example.library.server.BookScreenState
import kotlinx.coroutines.launch

class ShowMyBooksViewModel(
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
            _state.value = try {
                val book = bookApiRepository.getUserReadBooks()
                sessionManager.bookId = book.id
                BookScreenState.Book(book)
            } catch (e: Exception) {
                BookScreenState.Error(e.localizedMessage)
            }
        }
    }
}