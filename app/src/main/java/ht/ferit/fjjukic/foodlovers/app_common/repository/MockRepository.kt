package ht.ferit.fjjukic.foodlovers.app_common.repository

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel
import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TodayChoiceRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TopRecipe
import java.util.UUID

class MockRepository {

    companion object {

        private val categories = mutableListOf(
            CategoryModel(
                UUID.randomUUID().toString(),
                name = "Breakfast",
                drawableId = R.drawable.breakfast
            ),
            CategoryModel(
                UUID.randomUUID().toString(),
                name = "Dinner",
                drawableId = R.drawable.dinner
            ),
            CategoryModel(
                UUID.randomUUID().toString(),
                name = "Lunch",
                drawableId = R.drawable.lunch
            ),
            CategoryModel(
                UUID.randomUUID().toString(),
                name = "Salad",
                drawableId = R.drawable.salad
            ),
            CategoryModel(
                UUID.randomUUID().toString(),
                name = "Soup",
                drawableId = R.drawable.soup
            ),
            CategoryModel(
                UUID.randomUUID().toString(),
                name = "Desert",
                drawableId = R.drawable.desert
            )
        )

        private val difficulties = mutableListOf(
            DifficultyModel(UUID.randomUUID().toString(), name = "easy"),
            DifficultyModel(UUID.randomUUID().toString(), name = "medium"),
            DifficultyModel(UUID.randomUUID().toString(), name = "hard")
        )

        private val favoriteIds = listOf("1", "2")
        fun getCategories(): MutableList<CategoryModel> {
            return categories
        }

        private fun getCategoryID(category: String): CategoryModel {
            return categories.first { it.name.contains(category, true) }
        }

        private fun getDifficultyID(difficulty: String): DifficultyModel {
            return difficulties.first { it.name.contains(difficulty, true) }
        }

        fun getDifficulties(): List<DifficultyModel> {
            return difficulties
        }

        fun getRecipesDB(): List<RecipeModel> {
            return listOf(
                RecipeModel(
                    "5d384f88-1d76-4c55-922d-2692a39e096c",
                    "French Bread Pizza",
                    "Learn our tricks for the world's best snack.",
                    20,
                    4,
                    getSteps(),
                    getIngredients(),
                    getDifficultyID("easy"),
                    getCategoryID("dinner"),
                    UserModel(
                        isAdmin = false,
                        email = "test@mail.com",
                        latitude = "45.815399",
                        longitude = "15.966568",
                        name = "test123231",
                        userId = "rIJFSR081pXXfBszjaohhPfvtOP2"
                    ),
                    "https://www.simplyrecipes.com/thmb/XD1o4O89-VW9-1E_7o73tyvKqgA=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes-French-Bread-Pizza-LEAD-19-cb178af4707f4ab0ab20da5ba934c8be.jpg",

                    ),
                RecipeModel(
                    "0e69f0c3-f7dc-492b-8776-d4771db137f6",
                    "Skillet Tortilla Pizza",
                    "Super easy individual pizzas made with flour tortillas",
                    10,
                    4,
                    getSteps(),
                    getIngredients(),
                    getDifficultyID("easy"),
                    getCategoryID("lunch"),
                    UserModel(
                        isAdmin = false,
                        email = "test@mail.com",
                        latitude = "45.815399",
                        longitude = "15.966568",
                        name = "test123231",
                        userId = "rIJFSR081pXXfBszjaohhPfvtOP2"
                    ),
                    "https://www.simplyrecipes.com/thmb/_fbp0FSeQuSaQZFLQ6XkHtw03-0=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/__opt__aboutcom__coeus__resources__content_migration__simply_recipes__uploads__2012__05__skillet-tortilla-pizza-horiz-a-1800-c53514ad4c9d4ab2a487f20200389126.jpg",
                ),
                RecipeModel(
                    "73d42fe8-27b2-48ea-9ca5-3c908de3ee60",
                    "Burger on the Stove",
                    "No grill, no problemo!",
                    15,
                    4,
                    getSteps(),
                    getIngredients(),
                    getDifficultyID("easy"),
                    getCategoryID("dinner"),
                    UserModel(
                        isAdmin = false,
                        email = "test@mail.com",
                        latitude = "45.815399",
                        longitude = "15.966568",
                        name = "test123231",
                        userId = "rIJFSR081pXXfBszjaohhPfvtOP2"
                    ),
                    "https://www.simplyrecipes.com/thmb/5NbY2LUh2dvE3WzON6SZSLfjVG8=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes-Burger-On-Stovetop-LEAD-05-3c6cfb02ce7f492bb3f0bf847fc36460.jpg",
                ),
                RecipeModel(
                    "4474f91c-d3d2-4851-a634-a2e31a8da6f3",
                    "Pickle slaw",
                    "If you love traditional coleslaw, try this pickle slaw",
                    20,
                    4,
                    getSteps(),
                    getIngredients(),
                    getDifficultyID("easy"),
                    getCategoryID("breakfast"),
                    UserModel(
                        isAdmin = false,
                        email = "test@mail.com",
                        latitude = "45.815399",
                        longitude = "15.966568",
                        name = "test123231",
                        userId = "rIJFSR081pXXfBszjaohhPfvtOP2"
                    ),
                    "https://www.simplyrecipes.com/thmb/IPEwNrVPHKStUcsu36zmaP0UybE=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/__opt__aboutcom__coeus__resources__content_migration__simply_recipes__uploads__2020__08__Pickle-Slaw-LEAD-3-877e3ea3e67e412b9d463b3868108c78.jpg",
                ),
                RecipeModel(
                    "9d266567-69f0-4322-88f8-ce14a87da07a",
                    "Air Fryer Fish Sticks",
                    "Skip the freezer fish sticks and make these fresh, crispy fish sticks using your air fryer",
                    20,
                    4,
                    getSteps(),
                    getIngredients(),
                    getDifficultyID("easy"),
                    getCategoryID("dinner"),
                    UserModel(
                        isAdmin = false,
                        email = "test@mail.com",
                        latitude = "45.815399",
                        longitude = "15.966568",
                        name = "test123231",
                        userId = "rIJFSR081pXXfBszjaohhPfvtOP2"
                    ),
                    "https://www.simplyrecipes.com/thmb/y8gwKWgaNScMzjKXmBYw2pvGAD4=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes-Air-Fryer-Salmon-LEAD-08-1186c881fc8345aaaaba8c8d18782873.jpg",
                ),
                RecipeModel(
                    "48b4af49-a8a4-4754-9c5e-48ca7d632611",
                    "Strawberries and Cream",
                    "Semifreddo is the easiest, dreamiest frozen dessert!",
                    35,
                    4,
                    getSteps(),
                    getIngredients(),
                    getDifficultyID("easy"),
                    getCategoryID("desert"),
                    UserModel(
                        isAdmin = false,
                        email = "test@mail.com",
                        latitude = "45.815399",
                        longitude = "15.966568",
                        name = "test123231",
                        userId = "rIJFSR081pXXfBszjaohhPfvtOP2"
                    ),
                    "https://www.simplyrecipes.com/thmb/K30VrxTxgyCh31zjyEpfOouuovM=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/SimplyRecipes_StrawberriesandCreamSemifreddo_LEAD_7-f6f38ceceb664581a2e6c7508a0102cc.jpg",
                ),
                RecipeModel(
                    "4a0e0cdd-6b0d-4b3c-9552-6d9416ccfea9",
                    "Tex-Mex Chopped Chicken Salad",
                    "Seasoned chicken, tortilla chips, and roasted corn pair perfectly together",
                    35,
                    4,
                    getSteps(),
                    getIngredients(),
                    getDifficultyID("medium"),
                    getCategoryID("lunch"),
                    UserModel(
                        isAdmin = false,
                        email = "test@mail.com",
                        latitude = "45.815399",
                        longitude = "15.966568",
                        name = "test123231",
                        userId = "rIJFSR081pXXfBszjaohhPfvtOP2"
                    ),
                    "https://www.simplyrecipes.com/thmb/-eP-oTY6THaExCCX_HtVMPGuGIA=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes-Tex-Mex-Chopped-Chicken-LEAD-5-87fae3e1eaa543aeb2228daeab42f288.jpg",
                ),
                RecipeModel(
                    "75976e9c-0f8b-4af1-84a0-d87665a8dc89",
                    "Monkey Bread",
                    "Easy monkey bread recipe starts with store-bought biscuit dough",
                    50,
                    4,
                    getSteps(),
                    getIngredients(),
                    getDifficultyID("medium"),
                    getCategoryID("dinner"),
                    UserModel(
                        isAdmin = false,
                        email = "test@mail.com",
                        latitude = "45.815399",
                        longitude = "15.966568",
                        name = "test123231",
                        userId = "rIJFSR081pXXfBszjaohhPfvtOP2"
                    ),
                    "https://www.simplyrecipes.com/thmb/54qBbyzi8MGstUJImMz4LimaX9I=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes_Monkey-Bread_LEAD_9-480afdb0de5340819d35683a04e47577.jpg",
                )
            )
        }

        fun getTodayChoiceRecipes(): MutableList<HomeScreenRecipe> {
            return mutableListOf(
                TodayChoiceRecipe(
                    "1",
                    "French Bread Pizza",
                    "Learn our tricks for the world's best snack.",
                    "20",
                    4,
                    "easy",
                    "https://www.simplyrecipes.com/thmb/XD1o4O89-VW9-1E_7o73tyvKqgA=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes-French-Bread-Pizza-LEAD-19-cb178af4707f4ab0ab20da5ba934c8be.jpg",
                    "By REBECCA FIRKSER"
                ),
                TodayChoiceRecipe(
                    "2",
                    "Skillet Tortilla Pizza",
                    "Super easy individual pizzas made with flour tortillas",
                    "10",
                    4,
                    "easy",
                    "https://www.simplyrecipes.com/thmb/_fbp0FSeQuSaQZFLQ6XkHtw03-0=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/__opt__aboutcom__coeus__resources__content_migration__simply_recipes__uploads__2012__05__skillet-tortilla-pizza-horiz-a-1800-c53514ad4c9d4ab2a487f20200389126.jpg",
                    "By MAXINE BUILDER"
                ),
                TodayChoiceRecipe(
                    "3",
                    "Burger on the Stove",
                    "No grill, no problemo!",
                    "15",
                    4,
                    "easy",
                    "https://www.simplyrecipes.com/thmb/5NbY2LUh2dvE3WzON6SZSLfjVG8=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes-Burger-On-Stovetop-LEAD-05-3c6cfb02ce7f492bb3f0bf847fc36460.jpg",
                    "By STEPHANIE BURT"
                ),
                TodayChoiceRecipe(
                    "4",
                    "Pickle slaw",
                    "If you love traditional coleslaw, try this pickle slaw",
                    "20",
                    4,
                    "easy",
                    "https://www.simplyrecipes.com/thmb/IPEwNrVPHKStUcsu36zmaP0UybE=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/__opt__aboutcom__coeus__resources__content_migration__simply_recipes__uploads__2020__08__Pickle-Slaw-LEAD-3-877e3ea3e67e412b9d463b3868108c78.jpg",
                    "By JOY MONNERJAHN"
                ),
                TodayChoiceRecipe(
                    "5",
                    "Air Fryer Fish Sticks",
                    "Skip the freezer fish sticks and make these fresh, crispy fish sticks using your air fryer",
                    "20",
                    4,
                    "easy",
                    "https://www.simplyrecipes.com/thmb/y8gwKWgaNScMzjKXmBYw2pvGAD4=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes-Air-Fryer-Salmon-LEAD-08-1186c881fc8345aaaaba8c8d18782873.jpg",
                    "By DEVAN GRIMSRUD"
                )
            )
        }

        fun getTopRecipes(): MutableList<HomeScreenRecipe> {
            return mutableListOf(
                TopRecipe(
                    "6",
                    "Strawberries and Cream",
                    "Semifreddo is the easiest, dreamiest frozen dessert!",
                    "35",
                    4,
                    "easy",
                    "https://www.simplyrecipes.com/thmb/K30VrxTxgyCh31zjyEpfOouuovM=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/SimplyRecipes_StrawberriesandCreamSemifreddo_LEAD_7-f6f38ceceb664581a2e6c7508a0102cc.jpg",
                    "By MARY JO DILONARDO"
                ),
                TopRecipe(
                    "7",
                    "Tex-Mex Chopped Chicken Salad",
                    "Seasoned chicken, tortilla chips, and roasted corn pair perfectly together",
                    "35",
                    4,
                    "medium",
                    "https://www.simplyrecipes.com/thmb/-eP-oTY6THaExCCX_HtVMPGuGIA=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes-Tex-Mex-Chopped-Chicken-LEAD-5-87fae3e1eaa543aeb2228daeab42f288.jpg",
                    "By MARK BEAHM"
                ),
                TopRecipe(
                    "8",
                    "Monkey Bread",
                    "Easy monkey bread recipe starts with store-bought biscuit dough",
                    "50",
                    4,
                    "medium",
                    "https://www.simplyrecipes.com/thmb/54qBbyzi8MGstUJImMz4LimaX9I=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Simply-Recipes_Monkey-Bread_LEAD_9-480afdb0de5340819d35683a04e47577.jpg",
                    "By MARK BEAHM"
                )
            )
        }

        fun getRecipes(): MutableList<HomeScreenRecipe> {
            return mutableListOf<HomeScreenRecipe>().apply {
                addAll(getTopRecipes().mapToBasicRecipeModel())
                addAll(getTodayChoiceRecipes().mapToBasicRecipeModel())
            }.sortedBy { it.title }.toMutableList()
        }

        fun getFavoriteRecipes(): List<HomeScreenRecipe> {
            return getRecipes().mapToBasicRecipeModel().filter { favoriteIds.contains(it.id) }
                .sortedBy { it.title }
        }

        fun getRecipeByID(id: String): BasicRecipe? {
            return getRecipes().firstOrNull { it.id == id }?.let {
                BasicRecipe(
                    it.id,
                    it.title,
                    it.description,
                    it.time,
                    it.servings,
                    it.difficulty,
                    "",
                    it.imagePath,
                    it.user,
                    getIngredients(),
                    getSteps(),
                    favoriteIds.contains(it.id)
                )
            }
        }

        private fun getIngredients(): MutableList<IngredientModel> {
            return mutableListOf(
                IngredientModel(
                    "Granulated sugare",
                    "1 / 2 cup (100 g)"
                ),
                IngredientModel(
                    "Ground cinnamon",
                    "2 teaspoons"
                ),
                IngredientModel(
                    "2 (16.3-ounce)",
                    "biscuit dough"
                ),
                IngredientModel(
                    "3/4 cup (170g)",
                    "unsalted butter"
                ),
                IngredientModel(
                    "3/4 cup (160g)",
                    "packed light brown sugar"
                ),
                IngredientModel(
                    "1 teaspoon",
                    "vanilla extract"
                ),
            )
        }

        fun getSteps(): MutableList<StepModel> {
            return mutableListOf(
                StepModel(
                    1,
                    "Preheat the oven to 350°F"
                ),
                StepModel(
                    2,
                    "Prepare the cinnamon sugar and biscuit dough:\nIn a large bowl, whisk together the granulated sugar and cinnamon.\nSeparate the biscuit dough and cut each biscuit into 6 evenly sized pieces."
                ),
                StepModel(
                    3,
                    "Coat the biscuit dough:\nAdd the biscuit pieces to the bowl of cinnamon sugar, and use your hands to toss and evenly coat each piece with the cinnamon sugar. Transfer the coated dough and any extra cinnamon sugar into the prepared Bundt pan, and distribute them evenly in the pan."
                ),
                StepModel(
                    4,
                    "Melt the butter and brown sugar:\nIn a small saucepan over medium heat, melt the butter and brown sugar. Cook for a few minutes, stirring, until smooth and fully combined. Take the butter mixture off the heat and stir in the vanilla extract. Evenly pour the butter mixture over the dough."
                ),
                StepModel(
                    5,
                    "Bake:\nBake for 35 minutes, or until the top is golden brown and the caramel coating begins to bubble around the edges of the pan."
                ),
                StepModel(
                    6,
                    "Cool:\nRemove the monkey bread from the oven and let it cool in the pan set on a wire rack for about 10 minutes. Loosen the sides with a spatula or butter knife. Flip a large round plate or platter upside down over the pan. Then, carefully flip the pan. You may have pieces of dough stuck to the pan. That’s okay! Simply remove them and tuck them back into the monkey bread. Serve warm.\nMonkey bread is best served warm on the day it is baked. Any leftovers can be stored airtight at room temperature for 1 day, or in the refrigerator for up to 4 days. Reheat the monkey bread in a 300°F oven until warm to the touch, or heat individual servings in the microwave."
                ),
            )
        }

        private fun MutableList<HomeScreenRecipe>.mapToBasicRecipeModel(): List<BasicRecipe> {
            return map {
                BasicRecipe(
                    it.id,
                    it.title,
                    it.description,
                    it.time,
                    it.servings,
                    it.difficulty,
                    "",
                    it.imagePath,
                    it.user,
                    mutableListOf(),
                    mutableListOf(),
                    favoriteIds.contains(it.id)
                )
            }
        }
    }
}