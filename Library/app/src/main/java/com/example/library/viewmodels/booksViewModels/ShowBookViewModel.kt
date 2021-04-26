package com.example.library.viewmodels.booksViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.beans.serverModels.IN_LIBRARY
import com.example.library.beans.serverModels.PICKED_UP
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
            _state.value = BookScreenState.Loading
            val book = bookApiRepository.getBook()
            when (book.status) {
                IN_LIBRARY -> {
                    _state.value = try {
                        sessionManager.bookId = book.id
                        BookScreenState.Book(book)
                    } catch (e: Exception) {
                        BookScreenState.Error(e.localizedMessage)
                    }

                }
                PICKED_UP -> {
                    _state.value = try {
                        sessionManager.bookId = book.id
                        BookScreenState.HideDeadLine(book)
                    } catch (e: Exception) {
                        BookScreenState.Error(e.localizedMessage)
                    }
                }
                else -> {
                    _state.value = BookScreenState.HideButton(book)
                }
            }
        }
    }
}