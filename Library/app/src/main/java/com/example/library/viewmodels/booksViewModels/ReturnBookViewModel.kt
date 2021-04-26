package com.example.library.viewmodels.booksViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.R
import com.example.library.beans.serverModels.BooksData
import com.example.library.modules.StringHelper
import com.example.library.server.repositories.BooksRepository
import com.example.library.server.Resource
import kotlinx.coroutines.launch

class ReturnBookViewModel(
    private val stringHelper: StringHelper,
    private val bookApiRepository: BooksRepository
) : ViewModel() {

    private val returnBookStatus = MutableLiveData<Resource<Unit>>()
    val state: LiveData<Resource<Unit>>
        get() = returnBookStatus

    fun returnBook() {
        viewModelScope.launch {
            try {
                returnBookStatus.postValue(Resource.success(data = bookApiRepository.returnBook()))
            } catch (exception: Exception) {
                returnBookStatus.postValue(
                    Resource.error(
                        data = null,
                        message = exception.message ?: stringHelper.getStringFromRes(R.string.error)
                    )
                )
            }
        }
    }
}