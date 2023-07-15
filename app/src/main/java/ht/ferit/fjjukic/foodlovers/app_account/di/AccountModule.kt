package ht.ferit.fjjukic.foodlovers.app_account.di

import ht.ferit.fjjukic.foodlovers.app_account.viewmodel.AccountViewModel
import ht.ferit.fjjukic.foodlovers.app_account.viewmodel.ChangeEmailViewModel
import ht.ferit.fjjukic.foodlovers.app_account.viewmodel.ChangeLocationViewModel
import ht.ferit.fjjukic.foodlovers.app_account.viewmodel.ChangeUsernameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AccountModule = module {
    viewModel {
        AccountViewModel(get(), get(), get())
    }

    viewModel {
        ChangeUsernameViewModel(get(), get(), get())
    }

    viewModel {
        ChangeEmailViewModel(get(), get(), get())
    }

    viewModel {
        ChangeLocationViewModel(get(), get(), get())
    }
}