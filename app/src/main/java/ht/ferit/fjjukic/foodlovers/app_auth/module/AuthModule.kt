package ht.ferit.fjjukic.foodlovers.app_auth.module

import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel {
        AuthViewModel(get(), get(), get(), get())
    }
}