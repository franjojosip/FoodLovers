package ht.ferit.fjjukic.foodlovers.app_common.repository

import ht.ferit.fjjukic.foodlovers.app_recipe.model.Category
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TodayChoiceRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TopRecipe

class MockRepository {
    companion object {
        fun getCategories(): MutableList<HomeScreenRecipe> {
            return mutableListOf(
                Category("Breakfast"),
                Category("Lunch"),
                Category("Desert"),
                Category("Dinner"),
                Category("Soup"),
                Category("Salad")
            )
        }

        fun getTodayChoiceRecipes(): MutableList<HomeScreenRecipe> {
            return mutableListOf(
                TodayChoiceRecipe("1", "Recipe cooking title", "Example of description", "20 min", "laganini"),
                TodayChoiceRecipe("2", "Recipe cooking title", "Example of description", "20 min", "laganini"),
                TodayChoiceRecipe("3", "Recipe cooking title", "Example of description", "20 min", "laganini"),
                TodayChoiceRecipe("4", "Recipe cooking title", "Example of description", "20 min", "laganini"),
                TodayChoiceRecipe("5", "Recipe cooking title", "Example of description", "20 min", "laganini")
            )
        }

        fun getTopRecipes(): MutableList<HomeScreenRecipe> {
            return mutableListOf(
                TopRecipe("1", "Recipe cooking title", "20 min", "laganini", "Marko Maric"),
                TopRecipe("2","Recipe cooking title", "20 min", "laganini", "Marko Maric"),
                TopRecipe("3","Recipe cooking title", "20 min", "laganini", "Marko Maric")
            )
        }
    }
}