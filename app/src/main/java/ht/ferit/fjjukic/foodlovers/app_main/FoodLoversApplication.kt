package ht.ferit.fjjukic.foodlovers.app_main

import android.app.Application
import ht.ferit.fjjukic.foodlovers.app_account.di.AccountModule
import ht.ferit.fjjukic.foodlovers.app_auth.di.AuthModule
import ht.ferit.fjjukic.foodlovers.app_common.module.RepositoryModule
import ht.ferit.fjjukic.foodlovers.app_main.di.AppModule
import ht.ferit.fjjukic.foodlovers.app_main.di.MainModule
import ht.ferit.fjjukic.foodlovers.app_recipe.module.RecipeModule
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
            AppModule,
            RepositoryModule,
            MainModule,
            AuthModule,
            RecipeModule,
            AccountModule
        )
    }
}