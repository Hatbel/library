package com.example.library.viewmodels.booksViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.R
import com.example.library.beans.daoModels.DaoBook
import com.example.library.beans.serverModels.BooksData
import com.example.library.modules.StringHelper
import com.example.library.server.Resource
import com.example.library.server.repositories.BooksRepository
import kotlinx.coroutines.launch

class CreateBookViewModel(
    private val stringHelper: StringHelper,
    private val bookApiRepository: BooksRepository
) : ViewModel() {
    private fun isValidInput(name: String) = name != ""

    private val createBookStatus = MutableLiveData<Resource<DaoBook>>()
    val state: LiveData<Resource<DaoBook>>
        get() = createBookStatus


    fun createBook(name: String) {
        viewModelScope.launch {
            try {
                if (isValidInput(name)) {
                    createBookStatus.postValue(
                        Resource.success(
                            data = bookApiRepository.createBook(
                                name
                            )
                        )
                    )
                } else {
                    createBookStatus.postValue(
                        Resource.error(
                            data = null,
                            message = stringHelper.getStringFromRes(R.string.wrongData)
                        )
                    )
                }
            } catch (exception: Exception) {
                createBookStatus.postValue(
                    Resource.error(
                        data = null,
                        message = exception.message ?: stringHelper.getStringFromRes(R.string.error)
                    )
                )
            }
        }
    }
}