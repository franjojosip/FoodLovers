package ht.ferit.fjjukic.foodlovers.app_common.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.CategoryDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.DifficultyDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.RecipeDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.UserDao
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Category
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Difficulty
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Recipe
import ht.ferit.fjjukic.foodlovers.app_common.database.model.User
import ht.ferit.fjjukic.foodlovers.app_common.repository.MockRepository

@Database(
    entities = [Recipe::class, Difficulty::class, Category::class, User::class],
    version = 4,
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun difficultyDao(): DifficultyDao

    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: RecipeDatabase? = null
        private const val NAME = "recipe_db"

        fun getInstance(context: Context): RecipeDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = getDatabase(context)
                }
                return INSTANCE!!
            }
        }

        private fun getDatabase(
            context: Context
        ): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    NAME
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Thread { prepopulateDb(getInstance(context)) }.start()
                        }
                    })
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private fun prepopulateDb(db: RecipeDatabase) {
            db.categoryDao().insertAll(
                MockRepository.getCategories()
            )
            db.difficultyDao().insertAll(
                MockRepository.getDifficulties()
            )
            db.recipeDao().insertAll(
                MockRepository.getRecipesDB()
            )
        }
    }

}