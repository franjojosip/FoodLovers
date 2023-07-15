package ht.ferit.fjjukic.foodlovers.app_main.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import ht.ferit.fjjukic.foodlovers.app_common.database.Converters
import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.parser.GsonParser
import ht.ferit.fjjukic.foodlovers.app_common.database.parser.JsonParser
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProviderImpl
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDBImpl
import ht.ferit.fjjukic.foodlovers.app_common.notification.NotificationsManager
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManagerImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val AppModule = module {
    single { Gson() }
    single<JsonParser> { GsonParser(get()) }
    single { Converters(get()) }

    single<PreferenceManager> { PreferenceManagerImpl(get(), get()) }

    single<FirebaseDB> { FirebaseDBImpl(get()) }
    single { FirebaseAuth.getInstance() }
    single { FirebaseStorage.getInstance() }
    single<AnalyticsProvider> { AnalyticsProviderImpl(FirebaseAnalytics.getInstance(get())) }

    single { RecipeDatabase.getInstance(androidApplication(), get()) }

    single { NotificationsManager(get()) }
}