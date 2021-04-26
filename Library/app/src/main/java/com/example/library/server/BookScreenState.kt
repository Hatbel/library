package com.example.library.server

import com.example.library.beans.daoModels.DaoBook

sealed class BookScreenState {
    object Idle : BookScreenState()
    data class HideButton(val book: DaoBook) : BookScreenState()
    data class HideDeadLine(val book: DaoBook) : BookScreenState()
    object Loading : BookScreenState()
    data class Books(val books: List<DaoBook>) : BookScreenState()
    data class Book(val book: DaoBook) : BookScreenState()
    data class Error(val error: String?) : BookScreenState()
}