package com.example.library.viewmodels.booksViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.modules.SessionManager
import com.example.library.server.repositories.BooksRepository
import com.example.library.server.BookScreenState
import kotlinx.coroutines.launch

class OwnCreatedBooksViewModel(
    private val sessionManager: SessionManager,
    private val repository: BooksRepository
) : ViewModel() {
    private val _state = MutableLiveData<BookScreenState>()
    val state: LiveData<BookScreenState>
        get() = _state

    init {
        getListOfBooks()
    }

    fun savePosition(pos: Int) {
        sessionManager.bookId = pos
    }

    private fun getListOfBooks() {
        viewModelScope.launch {
            _state.value = try {
                val books = repository.getOwnBooks()
                BookScreenState.Books(books)
            } catch (e: Exception) {
                BookScreenState.Error(e.localizedMessage)
            }
        }
    }

}