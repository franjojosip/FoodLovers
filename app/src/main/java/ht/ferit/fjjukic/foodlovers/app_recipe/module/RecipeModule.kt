package ht.ferit.fjjukic.foodlovers.app_recipe.module

import ht.ferit.fjjukic.foodlovers.app_recipe.CategoryRecipesViewModel
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
        SearchViewModel(get(), get())
    }

    viewModel {
        CreateRecipeViewModel(get(), get(), get())
    }

    viewModel {
        ShowRecipeViewModel(get())
    }

    viewModel {
        RecipesViewModel(get())
    }

    viewModel {
        CategoryRecipesViewModel(get())
    }

    viewModel {
        FavoritesViewModel(get())
    }
}