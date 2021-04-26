package com.example.library.viewmodels.booksViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.beans.daoModels.DaoBook
import com.example.library.modules.SessionManager
import com.example.library.server.repositories.BooksRepository
import com.example.library.server.BookScreenState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BooksViewModel(
    private val sessionManager: SessionManager,
    private val repository: BooksRepository
) : ViewModel() {
    private var page = 1
    private var limit = 10
    private var isLoading = false
    private var books = mutableListOf<DaoBook>()

    private val _state = MutableLiveData<BookScreenState>()
    val state: LiveData<BookScreenState>
        get() = _state

    init {
        repository.observeBooks()
            .onEach {
                books.clear()
                books.addAll(it)
                _state.postValue(BookScreenState.Books(books))
            }
            .catch {
                _state.postValue(BookScreenState.Error(it.message))
            }
            .launchIn(viewModelScope)
        viewModelScope.launch {
            repository.loadBooks(limit, sessionManager.page)
        }
    }

    fun checkPosition(pos: Int) {
        if (pos == books.size - 1 && !isLoading) {
            isLoading = true
            sessionManager.page += 1
            viewModelScope.launch {
                repository.loadBooks(limit, sessionManager.page)
            }
        }
        isLoading = false
    }

    fun clear() {
        page = 0
        sessionManager.page = 1
        books.clear()
    }

    fun savePosition(pos: Int) {
        sessionManager.bookId = pos
    }

    fun getAvailableBooks(pos: Int) {
        repository.observeAvailableBooks()
            .onEach {
                books.clear()
                books.addAll(it)
                _state.postValue(BookScreenState.Books(books))
            }
            .catch {
                _state.postValue(BookScreenState.Error(it.message))
            }
            .launchIn(viewModelScope)

        if (pos == books.size - 1 && !isLoading) {
            isLoading = true
            sessionManager.page += 1
            viewModelScope.launch {
                repository.loadAvailableBooks(limit, sessionManager.page)
            }
        }
        isLoading = false
    }

    fun getExpiredBooks(pos: Int) {
        repository.observeExpiredBooks()
            .onEach {
                books.clear()
                books.addAll(it)
                _state.postValue(BookScreenState.Books(books))
            }
            .catch {
                _state.postValue(BookScreenState.Error(it.message))
            }
            .launchIn(viewModelScope)

        if (pos == books.size - 1 && !isLoading) {
            isLoading = true
            sessionManager.page += 1
            viewModelScope.launch {
                repository.loadExpiredBooks(limit, sessionManager.page)
            }
        }
        isLoading = false
    }

    fun getUserReadBooks() {
        viewModelScope.launch {
            _state.postValue(
                try {
                    books = repository.getReadBooks() as MutableList<DaoBook>
                    BookScreenState.Books(books)
                } catch (e: Exception) {
                    BookScreenState.Error(e.localizedMessage)
                }
            )
            isLoading = false
        }
    }
}