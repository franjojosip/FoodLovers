package ht.ferit.fjjukic.foodlovers.app_auth.di

import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.LoginViewModel
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.RegisterViewModel
import ht.ferit.fjjukic.foodlovers.app_auth.viewmodel.ResetPasswordViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AuthModule = module {
    viewModel {
        LoginViewModel(get(), get())
    }
    viewModel {
        RegisterViewModel(get(), get())
    }
    viewModel {
        ResetPasswordViewModel(get(), get())
    }
}