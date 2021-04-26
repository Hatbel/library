package com.example.library.viewmodels.userViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.R
import com.example.library.beans.daoModels.DaoUser
import com.example.library.modules.SessionManager
import com.example.library.modules.StringHelper
import com.example.library.server.Resource
import com.example.library.server.repositories.BooksRepository
import com.example.library.server.repositories.UserRepository
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val sessionManager: SessionManager,
    private val stringHelper: StringHelper,
    private val userApiRepository: UserRepository,
    private val bookApiRepository: BooksRepository
) : ViewModel() {

    private val profileStatus = MutableLiveData<Resource<DaoUser>>()
    val state: LiveData<Resource<DaoUser>>
        get() = profileStatus

    fun getUser() {
        profileStatus.postValue(Resource.loading())
        viewModelScope.launch {
            try {
                profileStatus.postValue(Resource.success(data = userApiRepository.getUser()))
            } catch (exception: Exception) {
                profileStatus.postValue(
                    Resource.error(
                        data = null, message = exception.message
                            ?: stringHelper.getStringFromRes(R.string.error)
                    )
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userApiRepository.clearTables()
            bookApiRepository.clearTables()
        }
        sessionManager.clearPrefs()
    }

}