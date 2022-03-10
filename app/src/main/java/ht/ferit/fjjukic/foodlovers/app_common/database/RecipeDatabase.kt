package ht.ferit.fjjukic.foodlovers.app_common.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.DifficultyLevelDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.FoodTypeDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.RecipeDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.UserDao
import ht.ferit.fjjukic.foodlovers.app_common.model.db.*

@Database(
    entities = [Comment::class, Recipe::class, DifficultyLevel::class, FoodType::class, User::class],
    version = 3,
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun difficultyLevelDao(): DifficultyLevelDao
    abstract fun foodTypeDao(): FoodTypeDao
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

        fun getDatabase(
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
                            Thread(Runnable { prepopulateDb(getInstance(context)) }).start()
                        }
                    })
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private fun prepopulateDb(db: RecipeDatabase) {
            db.difficultyLevelDao().insertAll(
                listOf(
                    DifficultyLevel("Easy"),
                    DifficultyLevel("Normal"),
                    DifficultyLevel("Hard")
                )
            )
            db.foodTypeDao().insertAll(listOf(FoodType("Sweet"), FoodType("Salty")))
        }
    }

}