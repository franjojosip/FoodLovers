package ht.ferit.fjjukic.foodlovers

import android.app.Application
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ht.ferit.fjjukic.foodlovers.data.database.FirebaseDB
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@FirebaseApplication))

        bind() from singleton { FirebaseSource() }
        bind() from singleton {
            UserRepository(FirebaseDB())
        }
        bind() from provider {
            UserViewModelFactory(
                instance(),
                UserRepository(FirebaseDB())
            )
        }
        bind() from provider {
            LocationViewModelFactory(
                instance(),
                UserRepository(FirebaseDB())
            )
        }
        bind() from provider {
            AuthViewModelFactory(
                instance(),
                UserRepository(FirebaseDB())
            )
        }
        bind() from provider {
            AccountViewModelFactory(
                instance(),
                UserRepository(FirebaseDB())
            )
        }
        bind() from provider {
            RecipeViewModelFactory(
                RecipeRepository(FirebaseDB())
            )
        }

    }
}