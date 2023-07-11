package ht.ferit.fjjukic.foodlovers.app_common.module

import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.favorites.FavoritesRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.favorites.FavoritesRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.filters.FilterRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.filters.FilterRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.resource.ResourceRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.resource.ResourceRepositoryImpl
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepositoryImpl
import org.koin.dsl.module

val commonModule = module {
    factory<ResourceRepository> { ResourceRepositoryImpl(get()) }

    factory<UserRepository> { UserRepositoryImpl(get(), get(), get(), get(), get()) }

    factory<FilterRepository> { FilterRepositoryImpl(get(), get()) }

    factory<FavoritesRepository> { FavoritesRepositoryImpl(get()) }

    factory<CategoryRepository> { CategoryRepositoryImpl(get(), get(), get()) }

    factory<DifficultyRepository> { DifficultyRepositoryImpl(get(), get(), get()) }

    factory<RecipeRepository> {
        RecipeRepositoryImpl(get(), get(), get(), get(), get(), get(), get())
    }
}