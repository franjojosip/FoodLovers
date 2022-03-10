package ht.ferit.fjjukic.foodlovers.app_main

import android.app.Application
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import ht.ferit.fjjukic.foodlovers.app_auth.module.authModule
import ht.ferit.fjjukic.foodlovers.app_common.module.commonModule
import ht.ferit.fjjukic.foodlovers.app_main.module.appModule
import ht.ferit.fjjukic.foodlovers.app_recipe.module.recipeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class FoodLoversApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@FoodLoversApplication)
            modules(getKoinModules())
        }
    }

    private fun getKoinModules(): List<Module> {
        return listOf(
            appModule,
            commonModule,
            authModule,
            recipeModule
        )
    }
}