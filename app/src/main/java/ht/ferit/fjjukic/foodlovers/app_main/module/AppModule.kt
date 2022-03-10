package ht.ferit.fjjukic.foodlovers.app_main.module

import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.app_common.notification.NotificationsManager
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManagerImpl
import ht.ferit.fjjukic.foodlovers.app_main.view_model.MainActivityViewModel
import ht.ferit.fjjukic.foodlovers.app_main.view_model.NavigationActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Gson() }
    single<PreferenceManager> { PreferenceManagerImpl(get(), get()) }
    single { FirebaseSource(get()) }
    single { FirebaseDB() }
    single { FirebaseStorage.getInstance() }
    single { NotificationsManager(get()) }

    viewModel {
        MainActivityViewModel(get())
    }
    viewModel {
        NavigationActivityViewModel(get(), get())
    }
}