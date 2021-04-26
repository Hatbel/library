package com.example.library.viewmodels.booksViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.R
import com.example.library.beans.daoModels.DaoBook
import com.example.library.beans.serverModels.ApiResponse
import com.example.library.beans.serverModels.BooksData
import com.example.library.modules.StringHelper
import com.example.library.server.BookScreenState
import com.example.library.server.repositories.BooksRepository
import com.example.library.server.Resource
import kotlinx.coroutines.launch

class AddedBookViewModel(
    private val stringHelper: StringHelper,
    private val bookApiRepository: BooksRepository
) : ViewModel() {
    private val addedBookStatus = MutableLiveData<Resource<DaoBook>>()
    val state: LiveData<Resource<DaoBook>>
        get() = addedBookStatus

    fun returnBook() {
        viewModelScope.launch {
            try {
                addedBookStatus.postValue(Resource.success(data = bookApiRepository.returnBookToOwner()))
            } catch (exception: Exception) {
                addedBookStatus.postValue(
                    Resource.error(
                        data = null, message = exception.message ?: stringHelper.getStringFromRes(
                            R.string.error
                        )
                    )
                )
            }
        }
    }
}