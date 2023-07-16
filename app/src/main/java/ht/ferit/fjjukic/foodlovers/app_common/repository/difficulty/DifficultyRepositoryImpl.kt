package ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToDifficulty
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToDifficultyModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToDifficultyModels
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@OptIn(DelicateCoroutinesApi::class)
class DifficultyRepositoryImpl(
    private val db: RecipeDatabase,
    private val firebaseDB: FirebaseDB,
    private val preferenceManager: PreferenceManager
) : DifficultyRepository {
    override fun init() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (preferenceManager.lastUpdatedCategories == 0L ||
                    db.difficultyDao().getAll().isEmpty()
                ) {
                    getDifficulties()
                }
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e)
            }
        }
    }

    override suspend fun getDifficulties(): Result<List<DifficultyModel>> {
        return withContext(Dispatchers.IO) {
            val difficulties = db.difficultyDao().getAll()

            when {
                hasDayPassed() || difficulties.isEmpty() -> {
                    val newDifficulties = firebaseDB.getDifficulties().getOrDefault(listOf())

                    if (newDifficulties.isNotEmpty()) {
                        saveDifficulties(newDifficulties)
                        Result.success(newDifficulties)
                    } else {
                        Result.success(mapToDifficultyModels(difficulties))
                    }
                }

                else -> {
                    Result.success(mapToDifficultyModels(difficulties))
                }
            }
        }
    }

    private fun hasDayPassed(): Boolean {
        return (System.currentTimeMillis() - preferenceManager.lastUpdatedDifficulties) >= TimeUnit.DAYS.toMillis(
            1
        )
    }

    private fun saveDifficulties(data: List<DifficultyModel>) {
        preferenceManager.lastUpdatedDifficulties = System.currentTimeMillis()
        data.forEach {
            db.difficultyDao().insert(it.mapToDifficulty())
        }
    }

    override suspend fun updateDifficulty(difficulty: DifficultyModel): Result<DifficultyModel> {
        return withContext(Dispatchers.IO) {
            val result = firebaseDB.updateDifficulty(difficulty)

            if (result.isSuccess) {
                saveDifficulty(difficulty)
                result
            } else {
                result
            }
        }
    }

    override suspend fun createDifficulty(difficulty: DifficultyModel): Result<DifficultyModel> {
        return withContext(Dispatchers.IO) {
            val result = firebaseDB.createDifficulty(difficulty)

            if (result.isSuccess) {
                saveDifficulty(difficulty)
                result
            } else {
                result
            }
        }
    }

    override suspend fun deleteDifficulty(id: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            db.difficultyDao().delete(id)
            firebaseDB.deleteDifficulty(id)
        }
    }

    override suspend fun getFilterDifficulties(): List<FilterItem.Difficulty> {
        return getDifficulties().getOrDefault(listOf()).let { models ->
            val difficulties =
                mutableListOf(FilterItem.Difficulty("All", isChecked = true, isDefault = true))
            difficulties.addAll(models.map { FilterItem.Difficulty(it.name) })
            difficulties
        }
    }

    override suspend fun getDifficulty(id: String): Result<DifficultyModel?> {
        return withContext(Dispatchers.IO) {
            val difficulty = db.difficultyDao().get(id)?.mapToDifficultyModel()

            if (difficulty != null) {
                Result.success(difficulty)
            } else {
                val firebaseDifficulty = firebaseDB.getDifficulty(id).getOrNull()

                if (firebaseDifficulty != null) {
                    saveDifficulty(firebaseDifficulty)
                    Result.success(firebaseDifficulty)
                } else {
                    Result.failure(Exception("Error while retrieving data"))
                }
            }
        }
    }

    private fun saveDifficulty(difficulty: DifficultyModel) {
        db.difficultyDao().insert(difficulty.mapToDifficulty())
    }
}