package com.example.library.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.example.library.beans.serverModels.*
import com.example.library.database.MyDatabase
import com.example.library.interfaces.apiInterfaces.BookApi
import com.example.library.interfaces.apiInterfaces.UserApi
import com.example.library.modules.SessionManager
import com.example.library.modules.StringHelper
import com.example.library.server.repositories.BooksRepository
import com.example.library.server.repositories.UserRepository
import com.example.library.viewmodels.userViewModels.LoginViewModel
import com.example.library.viewmodels.userViewModels.RegisterViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ViewModelTests : KoinTest{
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var registerViewModel: RegisterViewModel

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val bookRepo: BooksRepository by inject()
    private val userRepo: UserRepository by inject()
    private val stringHelper: StringHelper by inject()
    private val sessionManager: SessionManager by inject()

    @Mock
    private lateinit var userApiHelper: UserApi
    @Mock
    private lateinit var bookApiHelper: BookApi
    @Mock
    private lateinit var mockContext: Context


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(
                module {
                    single { StringHelper(mockContext) }
                    single { SessionManager(mockContext) }
                    single { Room.databaseBuilder(mockContext, MyDatabase::class.java, "MyDataBase").build() }
                    single { get<MyDatabase>().userDao() }
                    single { UserRepository(userApiHelper,get(),get(),get()) }
                    single { BooksRepository(bookApiHelper,get(),get()) }
                })
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Before
    fun setUp() = runBlocking{
        Dispatchers.setMain(mainThreadSurrogate)
        loginViewModel = LoginViewModel(stringHelper, userRepo)
        registerViewModel = RegisterViewModel(stringHelper, userRepo)
    }

    @Test
    fun userViewModelsTest() = runBlocking<Unit> {
        val response = ApiResponse(200, "OK", UserData(0, "mail", "token"))
        val request = LoginRegisterModel("mail","password")
        `when`(userApiHelper.login(request))
                .thenReturn(response)
        `when`(userApiHelper.createUser(request))
                .thenReturn(response)
        `when`(userApiHelper.getUser(0))
                .thenReturn(response)
    }
    @Test
    fun bookViewModelsTest() = runBlocking<Unit> {
        val response = ApiResponse(200, "OK", BooksData(0, "name",0,"status","deadLine",0,false))
        val responses = ApiResponse(200, "OK", List(1){ BooksData(0, "name",0,"status","deadLine",0,false) })
        val request = CreateBookModel("name")
        `when`(bookApiHelper.createBook(request))
                .thenReturn(response)
        `when`(bookApiHelper.getAvailableBooks(1,0))
                .thenReturn(responses)
        `when`(bookApiHelper.getExpiredBooks(1,0))
                .thenReturn(responses)
        `when`(bookApiHelper.getListOfBooks(1,0))
                .thenReturn(responses)
        `when`(bookApiHelper.getOwnBooks())
                .thenReturn(responses)
        `when`(bookApiHelper.getUserReadBook())
                .thenReturn(response)
        `when`(bookApiHelper.reserveBook(0))
                .thenReturn(response)
        `when`(bookApiHelper.returnBook(0))
                .thenReturn(Unit)
        `when`(bookApiHelper.getBook(0))
                .thenReturn(response)
        `when`(bookApiHelper.returnBook(0))
                .thenReturn(Unit)
    }
    @Test
    fun loginParamsCheck() = runBlocking<Unit> {
        val email = "email"
        val password = "password"
        `when`(loginViewModel.isValidInput(email, password))
                .thenReturn(true)
    }
    @Test
    fun registerParamsCheck() = runBlocking<Unit> {
        val email = "email"
        val password = "password"
        `when`(registerViewModel.isValidInput(email, password))
                .thenReturn(true)
    }
}