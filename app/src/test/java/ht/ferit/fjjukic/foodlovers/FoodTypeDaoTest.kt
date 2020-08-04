package ht.ferit.fjjukic.foodlovers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ht.ferit.fjjukic.foodlovers.data.database.FoodTypeDao
import ht.ferit.fjjukic.foodlovers.data.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.data.model.FoodType
import ht.ferit.fjjukic.foodlovers.observer.OneTimeObserver
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config (manifest = Config.NONE)
class FoodTypeDaoTest {
    private var database: RecipeDatabase? = null
    private var dao: FoodTypeDao? = null

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = database!!.foodTypeDao()
    }

    @Test
    @Throws(Exception::class)
    fun insert() {
        val foodType = FoodType("Sweet")
        dao!!.insert(foodType)
        dao!!.getAll().observeOnce {
            if(it.isNullOrEmpty()){
                throw IllegalArgumentException("Value from database is null or empty")
            }
            Assert.assertEquals(1, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun get() {
        val foodType = FoodType("Sweet")
        dao!!.insert(foodType)
        dao!!.getAll().observeOnce {
            if(it.isNullOrEmpty()){
                throw IllegalArgumentException("Value from database is null or empty")
            }
            Assert.assertEquals(1, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        val foodType = FoodType("Sweet")
        dao!!.insert(foodType)
        foodType.name = "Salty"
        dao!!.update(foodType)
        dao!!.getAll().observeOnce {
            if(it.isNullOrEmpty()){
                throw IllegalArgumentException("Value from database is null or empty")
            }
            Assert.assertEquals(1, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        val foodType = FoodType("Hard")
        dao!!.insert(foodType)
        dao!!.deleteAll()
        dao!!.getAll().observeOnce {
            if(it == null){
                throw IllegalArgumentException("Value from database is null")
            }
            Assert.assertEquals(0, it.size)
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        database!!.close()
    }

    private fun <T> LiveData<T>.observeOnce(onChangeHandler: (T?) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }
}
