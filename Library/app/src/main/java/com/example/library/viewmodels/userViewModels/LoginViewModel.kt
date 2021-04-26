package com.example.library.viewmodels.userViewModels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.R
import com.example.library.beans.daoModels.DaoUser
import com.example.library.beans.serverModels.LoginRegisterModel
import com.example.library.modules.StringHelper
import com.example.library.server.Resource
import com.example.library.server.repositories.UserRepository
import kotlinx.coroutines.launch

private const val MINIMAL_PASSWORD_LENGTH = 5

class LoginViewModel(
    private val stringHelper: StringHelper,
    private val userApiRepository: UserRepository
) : ViewModel() {
    private val loginStatus = MutableLiveData<Resource<DaoUser>>()
    val state: LiveData<Resource<DaoUser>>
        get() = loginStatus

    fun login(email: String, password: String) {
        loginStatus.postValue(Resource.loading())
        viewModelScope.launch {
            try {
                if (isValidInput(email, password)) {
                    val user = LoginRegisterModel(email, password)
                    loginStatus.postValue(Resource.success(data = userApiRepository.login(user)))
                } else {
                    loginStatus.postValue(
                        Resource.error(
                            data = null,
                            message = stringHelper.getStringFromRes(R.string.wrongData)
                        )
                    )
                }
            } catch (exception: Exception) {
                loginStatus.postValue(
                    Resource.error(
                        data = null,
                        message = exception.message ?: stringHelper.getStringFromRes(R.string.error)
                    )
                )
            }
        }
    }

    fun isValidInput(email: String, password: String) =
        password.length > MINIMAL_PASSWORD_LENGTH && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}