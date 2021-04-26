package com.example.library.server.repositories

import com.example.library.beans.daoModels.DaoUser
import com.example.library.beans.serverModels.ApiResponse
import com.example.library.beans.serverModels.LoginRegisterModel
import com.example.library.beans.serverModels.UserData
import com.example.library.interfaces.apiInterfaces.UserApi
import com.example.library.interfaces.daoInterfaces.UserDao
import com.example.library.modules.SessionManager

class UserRepository(
    private val userApi: UserApi,
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) {
    private lateinit var authUser: ApiResponse<UserData>

    suspend fun createUser(user: LoginRegisterModel): DaoUser {
        authUser = userApi.createUser(user)
        sessionManager.userId = authUser.data.id
        sessionManager.token = authUser.data.token
        userDao.insertUser(authUser.data.toDaoUser())
        return authUser.data.toDaoUser()
    }

    suspend fun login(user: LoginRegisterModel): DaoUser {
        authUser = userApi.login(user)
        sessionManager.userId = authUser.data.id
        sessionManager.token = authUser.data.token
        userDao.insertUser(authUser.data.toDaoUser())
        return authUser.data.toDaoUser()
    }

    suspend fun getUser(): DaoUser {
        return try {
            userApi.getUser(sessionManager.userId).data.toDaoUser()
        } catch (e: Exception) {
            userDao.getUserById(sessionManager.userId)

        }
    }

    suspend fun clearTables() {
        userDao.clearUserTable()
    }

    fun UserData.toDaoUser(): DaoUser =
        DaoUser(
            id,
            mail
        )
}