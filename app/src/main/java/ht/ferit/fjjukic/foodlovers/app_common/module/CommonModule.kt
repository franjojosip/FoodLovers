package ht.ferit.fjjukic.foodlovers.app_common.module

import ht.ferit.fjjukic.foodlovers.app_account.change_email.view_model.ChangeEmailViewModel
import ht.ferit.fjjukic.foodlovers.app_account.change_username.view_model.ChangeUsernameViewModel
import ht.ferit.fjjukic.foodlovers.app_account.view_model.AccountViewModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.FilterRepositoryMock
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.resource.ResourceRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.resource.ResourceRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_location.viewmodel.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    factory<ResourceRepository> { ResourceRepositoryImpl(get()) }

    factory<CategoryRepository> { CategoryRepositoryImpl(get(), get(), get()) }
    factory<DifficultyRepository> { DifficultyRepositoryImpl(get(), get(), get()) }

    factory<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    factory<RecipeRepository> {
        RecipeRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory { FilterRepositoryMock() }

    viewModel {
        LocationViewModel(get(), get())
    }

    viewModel {
        AccountViewModel(get(), get())
    }

    viewModel {
        ChangeUsernameViewModel(get(), get())
    }

    viewModel {
        ChangeEmailViewModel(get(), get())
    }
}