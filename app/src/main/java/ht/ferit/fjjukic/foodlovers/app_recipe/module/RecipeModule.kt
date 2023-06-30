package ht.ferit.fjjukic.foodlovers.app_recipe.module

import ht.ferit.fjjukic.foodlovers.app_recipe.RecipesViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.create_recipe.CreateRecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.favorites.FavoritesViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.home.HomeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.search.SearchViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe.ShowRecipeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val recipeModule = module {
    viewModel {
        HomeViewModel(get(), get())
    }
    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        CreateRecipeViewModel(get())
    }

    viewModel {
        ShowRecipeViewModel(get())
    }

    viewModel {
        RecipesViewModel()
    }

    viewModel {
        FavoritesViewModel()
    }
}