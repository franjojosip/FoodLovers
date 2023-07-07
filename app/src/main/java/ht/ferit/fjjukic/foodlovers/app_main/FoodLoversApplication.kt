package ht.ferit.fjjukic.foodlovers.app_main

import android.app.Application
import ht.ferit.fjjukic.foodlovers.app_account.di.accountModule
import ht.ferit.fjjukic.foodlovers.app_auth.di.authModule
import ht.ferit.fjjukic.foodlovers.app_common.module.commonModule
import ht.ferit.fjjukic.foodlovers.app_main.di.appModule
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
            authModule,
            commonModule,
            accountModule,
            recipeModule
        )
    }
}