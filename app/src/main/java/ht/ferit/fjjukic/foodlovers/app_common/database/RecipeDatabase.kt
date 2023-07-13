package ht.ferit.fjjukic.foodlovers.app_common.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.CategoryDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.DifficultyDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.RecipeDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.UserDao
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Category
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Difficulty
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Recipe
import ht.ferit.fjjukic.foodlovers.app_common.database.model.User

@Database(
    entities = [Recipe::class, Difficulty::class, Category::class, User::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun difficultyDao(): DifficultyDao

    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: RecipeDatabase? = null
        private const val NAME = "recipe_db"

        fun getInstance(context: Context, converters: Converters): RecipeDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = getDatabase(context, converters)
                }
                return INSTANCE as RecipeDatabase
            }
        }

        private fun getDatabase(
            context: Context,
            converters: Converters
        ): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    NAME
                )
                    .fallbackToDestructiveMigration()
                    .addTypeConverter(converters)
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}