package ht.ferit.fjjukic.foodlovers.app_auth.di

import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.LoginViewModel
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.RegisterViewModel
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.ResetPasswordViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        RegisterViewModel(get())
    }
    viewModel {
        ResetPasswordViewModel(get())
    }
}