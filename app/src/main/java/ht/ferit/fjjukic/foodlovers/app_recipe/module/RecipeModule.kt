package ht.ferit.fjjukic.foodlovers.app_recipe.module

import ht.ferit.fjjukic.foodlovers.app_recipe.category_recipes.CategoryRecipesViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.create_edit_recipe.RecipeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.favorites.FavoritesViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.home.HomeViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.recipes.RecipesViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.search.SearchViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe.ShowRecipeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RecipeModule = module {
    viewModel {
        HomeViewModel(get(), get(), get())
    }
    viewModel {
        SearchViewModel(get(), get(), get())
    }

    viewModel { (recipeId: String?) ->
        RecipeViewModel(get(), get(), get(), get(), recipeId)
    }

    viewModel { (recipeId: String) ->
        ShowRecipeViewModel(get(), get(), get(), recipeId)
    }

    viewModel {
        RecipesViewModel(get(), get())
    }

    viewModel { (category: String) ->
        CategoryRecipesViewModel(get(), get(), category)
    }

    viewModel {
        FavoritesViewModel(get(), get())
    }
}