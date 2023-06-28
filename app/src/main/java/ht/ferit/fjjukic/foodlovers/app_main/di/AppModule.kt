package ht.ferit.fjjukic.foodlovers.app_main.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDBImpl
import ht.ferit.fjjukic.foodlovers.app_common.notification.NotificationsManager
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManagerImpl
import ht.ferit.fjjukic.foodlovers.app_main.viewmodel.WelcomeScreenViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Gson() }
    single<PreferenceManager> { PreferenceManagerImpl(get(), get()) }
    single<FirebaseDB> { FirebaseDBImpl() }
    single { FirebaseAuth.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { RecipeDatabase.getInstance(androidApplication()) }
    single { NotificationsManager(get()) }

    viewModel {
        WelcomeScreenViewModel(get())
    }
}