package com.example.library.modules

import androidx.room.Room
import com.example.library.database.MyDatabase
import com.example.library.server.repositories.BooksRepository
import com.example.library.server.repositories.UserRepository
import com.example.library.server.retrofit.RetrofitBuilder
import com.example.library.viewmodels.booksViewModels.*
import com.example.library.viewmodels.userViewModels.LoginViewModel
import com.example.library.viewmodels.userViewModels.ProfileViewModel
import com.example.library.viewmodels.userViewModels.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { SessionManager(androidContext()) }
    single { StringHelper(androidContext()) }
    single { DeepLinkManager(get()) }
}
val dataBaseModule = module {
    single { get<MyDatabase>().userDao() }
    single { get<MyDatabase>().bookDao() }
    single { Room.databaseBuilder(androidContext(), MyDatabase::class.java, "MyDataBase").build() }
}
val viewModelsModule = module {
    viewModel { BooksViewModel(get(), get()) }
    viewModel { CreateBookViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { ReserveBookViewModel(get(), get()) }
    viewModel { ReturnBookViewModel(get(), get()) }
    viewModel { ShowMyBooksViewModel(get(), get()) }
    viewModel { ShowBookViewModel(get(), get()) }
    viewModel { AddedBookViewModel(get(), get()) }
    viewModel { OwnCreatedBooksViewModel(get(), get()) }
}
val networkModule = module {
    single { RetrofitBuilder(get()).getUserApiService() }
    single { RetrofitBuilder(get()).getBookApiService() }

}
val repositoriesModel = module {
    single { UserRepository(get(), get(), get()) }
    single { BooksRepository(get(), get(), get()) }
}


