package ht.ferit.fjjukic.foodlovers

import android.app.Application
import android.content.Context
import ht.ferit.fjjukic.foodlovers.data.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.data.repository.DifficultyLevelRepository
import ht.ferit.fjjukic.foodlovers.data.repository.FoodTypeRepository
import ht.ferit.fjjukic.foodlovers.data.repository.RecipeRepository
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import ht.ferit.fjjukic.foodlovers.ui.base.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class FirebaseApplication : Application(), KodeinAware {

    companion object {
        lateinit var ApplicationContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationContext = this
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@FirebaseApplication))

        bind() from singleton { FirebaseSource() }
        bind() from singleton {
            UserRepository(
                instance(),
                RecipeDatabase.getDatabase(applicationContext).userDao()
            )
        }
        bind() from provider { UserViewModelFactory(instance()) }
        bind() from provider { LocationViewModelFactory(
            UserRepository(
                instance(),
                RecipeDatabase.getDatabase(applicationContext).userDao()
            )) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider {
            AccountViewModelFactory(
                UserRepository(
                    instance(),
                    RecipeDatabase.getDatabase(applicationContext).userDao()
                )
            )
        }
        bind() from provider {
            MenuViewModelFactory(
                FoodTypeRepository(
                    RecipeDatabase.getDatabase(
                        applicationContext
                    ).foodTypeDao()
                )
            )
        }
        bind() from provider {
            HomeViewModelFactory(
                UserRepository(
                    instance(),
                    RecipeDatabase.getDatabase(applicationContext).userDao()
                )
            )
        }
        bind() from provider {
            RecipeViewModelFactory(
                RecipeRepository(RecipeDatabase.getDatabase(applicationContext).recipeDao()),
                FoodTypeRepository(RecipeDatabase.getDatabase(applicationContext).foodTypeDao()),
                DifficultyLevelRepository(
                    RecipeDatabase.getDatabase(applicationContext).difficultyLevelDao()
                )
            )
        }

    }
}