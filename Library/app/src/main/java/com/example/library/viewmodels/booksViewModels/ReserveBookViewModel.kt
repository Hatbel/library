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
import com.example.library.server.repositories.BooksRepository
import com.example.library.server.Resource
import kotlinx.coroutines.launch

class ReserveBookViewModel(
    private val stringHelper: StringHelper,
    private val bookApiRepository: BooksRepository
) : ViewModel() {

    private val reserveBookStatus = MutableLiveData<Resource<DaoBook>>()
    val state: LiveData<Resource<DaoBook>>
        get() = reserveBookStatus

    fun reserveBook() {
        viewModelScope.launch {
            try {
                reserveBookStatus.postValue(Resource.success(data = bookApiRepository.reserveBook()))
            } catch (exception: Exception) {
                reserveBookStatus.postValue(
                    Resource.error(
                        data = null,
                        message = exception.message ?: stringHelper.getStringFromRes(R.string.error)
                    )
                )
            }
        }
    }
}