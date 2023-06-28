package ht.ferit.fjjukic.foodlovers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.dao.DifficultyLevelDao
import ht.ferit.fjjukic.foodlovers.observer.observeOnce
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config (manifest = Config.NONE)
class DifficultyLevelDaoTest {
    private lateinit var database: RecipeDatabase
    private lateinit var dao: DifficultyLevelDao

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = database.difficultyLevelDao()
    }

    @Test
    @Throws(Exception::class)
    fun insert() {
        val difficultyLevel = DifficultyLevel("Hard")
        dao.insert(difficultyLevel)
        dao.getAll().observeOnce {
            if(it.isNullOrEmpty()){
                throw IllegalArgumentException("Value from database is null or empty")
            }
            Assert.assertEquals(1, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun get() {
        val difficultyLevel = DifficultyLevel("Hard")
        dao.insert(difficultyLevel)
        dao.getAll().observeOnce {
            if(it.isNullOrEmpty()){
                throw IllegalArgumentException("Value from database is null or empty")
            }
            Assert.assertEquals(1, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        val difficultyLevel = DifficultyLevel("Hard")
        dao.insert(difficultyLevel)
        difficultyLevel.name = "Easy"
        dao.update(difficultyLevel)
        dao.getAll().observeOnce {
            if(it.isNullOrEmpty()){
                throw IllegalArgumentException("Value from database is null or empty")
            }
            Assert.assertEquals(1, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        val difficultyLevel = DifficultyLevel("Hard")
        dao.insert(difficultyLevel)
        dao.deleteAll()
        dao.getAll().observeOnce {
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
