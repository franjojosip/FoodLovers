package ht.ferit.fjjukic.foodlovers.app_main.di

import ht.ferit.fjjukic.foodlovers.app_main.main.MainActivityViewModel
import ht.ferit.fjjukic.foodlovers.app_main.prelogin.PreloginViewModel
import ht.ferit.fjjukic.foodlovers.app_main.welcome_screen.WelcomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MainModule = module {
    viewModel {
        WelcomeScreenViewModel(get())
    }
    viewModel {
        PreloginViewModel(get(), get(), get())
    }
    viewModel {
        MainActivityViewModel(get(), get(), get())
    }
}