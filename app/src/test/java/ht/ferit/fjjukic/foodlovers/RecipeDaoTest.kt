package ht.ferit.fjjukic.foodlovers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.DifficultyLevelDao
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.RecipeDao
import ht.ferit.fjjukic.foodlovers.app_common.database.model.FoodType
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Recipe
import ht.ferit.fjjukic.foodlovers.observer.observeOnce
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config (manifest = Config.NONE)
class RecipeDaoTest {
    private lateinit var database: RecipeDatabase
    private lateinit var recipeDao: RecipeDao
    private lateinit var foodTypeDao: FoodTypeDao
    private lateinit var difficultyLevelDao: DifficultyLevelDao

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java)
            .allowMainThreadQueries().build()
        recipeDao = database.recipeDao()
        foodTypeDao = database.foodTypeDao()
        difficultyLevelDao = database.difficultyLevelDao()
    }

    @Test
    @Throws(Exception::class)
    fun insert() {
        val recipe = Recipe("Grah", "Recept za grah varivo kako ga kuhaju naše mame i bake.", "", 1, 1)
        val foodType = FoodType("Salty")
        val difficultyLevel = DifficultyLevel("Easy")
        foodTypeDao.insert(foodType)
        difficultyLevelDao.insert(difficultyLevel)
        recipeDao.insert(recipe)
        recipeDao.getAll().observeOnce {
            if(it.isNullOrEmpty()){
                throw IllegalArgumentException("Value from database is null or empty")
            }
            Assert.assertEquals(1, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun get() {
        val recipe = Recipe("Grah", "Recept za grah varivo kako ga kuhaju naše mame i bake.", "", 1, 1)
        val foodType = FoodType("Salty")
        val difficultyLevel = DifficultyLevel("Easy")
        foodTypeDao.insert(foodType)
        difficultyLevelDao.insert(difficultyLevel)
        recipeDao.insert(recipe)
        recipeDao.getAll().observeOnce {
            if(it.isNullOrEmpty()){
                throw IllegalArgumentException("Value from database is null or empty")
            }
            Assert.assertEquals(1, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        val recipe = Recipe("Grah", "Recept za grah varivo kako ga kuhaju naše mame i bake.", "", 1, 1)
        val foodType = FoodType("Salty")
        val difficultyLevel = DifficultyLevel("Easy")
        foodTypeDao.insert(foodType)
        difficultyLevelDao.insert(difficultyLevel)
        recipeDao.insert(recipe)
        recipe.name = "Grah po domaći"
        recipeDao.update(recipe)
        recipeDao.getAll().observeOnce {
            if(it.isNullOrEmpty()){
                throw IllegalArgumentException("Value from database is null or empty")
            }
            Assert.assertEquals(1, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        val recipe = Recipe("Grah", "Recept za grah varivo kako ga kuhaju naše mame i bake.", "", 1, 1)
        val foodType = FoodType("Salty")
        val difficultyLevel = DifficultyLevel("Easy")
        foodTypeDao.insert(foodType)
        difficultyLevelDao.insert(difficultyLevel)
        recipeDao.insert(recipe)
        recipeDao.deleteAll()
        recipeDao.getAll().observeOnce {
            if(it == null){
                throw IllegalArgumentException("Value from database is null")
            }
            Assert.assertEquals(0, it.size)
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        database.close()
    }
}
