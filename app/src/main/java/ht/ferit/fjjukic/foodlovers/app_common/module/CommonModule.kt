package ht.ferit.fjjukic.foodlovers.app_common.module

import ht.ferit.fjjukic.foodlovers.app_account.change_email.view_model.ChangeEmailViewModel
import ht.ferit.fjjukic.foodlovers.app_account.change_username.view_model.ChangeUsernameViewModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty_level.DifficultyLevelRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty_level.DifficultyLevelRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.food_type.FoodTypeRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.food_type.FoodTypeRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.resource.ResourceRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.resource.ResourceRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_account.view_model.AccountViewModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.FilterRepositoryMock
import ht.ferit.fjjukic.foodlovers.app_location.viewmodel.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    factory<ResourceRepository> { ResourceRepositoryImpl(get()) }
    factory<UserRepository> { UserRepositoryImpl(get(), get()) }
    factory<RecipeRepository> { RecipeRepositoryImpl(get()) }
    factory<FoodTypeRepository> { FoodTypeRepositoryImpl(get()) }
    factory<DifficultyLevelRepository> { DifficultyLevelRepositoryImpl(get()) }
    factory { FilterRepositoryMock() }

    viewModel {
        LocationViewModel(get(), get())
    }

    viewModel {
        AccountViewModel(get(), get(), get())
    }

    viewModel {
        ChangeUsernameViewModel(get(), get())
    }

    viewModel {
        ChangeEmailViewModel(get(), get(), get())
    }
}