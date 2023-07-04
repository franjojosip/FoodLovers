package ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty

import ht.ferit.fjjukic.foodlovers.app_common.database.RecipeDatabase
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Difficulty
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class DifficultyRepositoryImpl(
    private val db: RecipeDatabase,
    private val firebaseDB: FirebaseDB,
    private val preferenceManager: PreferenceManager
) : DifficultyRepository {
    override suspend fun getDifficulties(): Result<List<DifficultyModel>> {
        return withContext(Dispatchers.IO) {
            val difficulties = db.difficultyDao().getAll()

            when {
                hasDayPassed() -> {
                    val newDifficulties = firebaseDB.getDifficulties().getOrDefault(listOf())

                    if (newDifficulties.isNotEmpty()) {
                        saveDifficulties(newDifficulties)
                        Result.success(newDifficulties)
                    } else {
                        Result.success(mapToDifficultyModels(difficulties))
                    }
                }

                difficulties.isNotEmpty() -> {
                    Result.success(mapToDifficultyModels(difficulties))
                }

                else -> {
                    val newDifficulties = firebaseDB.getDifficulties().getOrDefault(listOf())
                    saveDifficulties(newDifficulties)

                    Result.success(newDifficulties)
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
            difficulties.addAll(models.map { FilterItem.Difficulty(it.name.replaceFirstChar(Char::titlecase)) })
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

    private fun mapToDifficultyModels(data: List<Difficulty>): List<DifficultyModel> {
        val difficulties = mutableListOf<DifficultyModel>()

        data.forEach {
            difficulties.add(it.mapToDifficultyModel())
        }
        return difficulties
    }

    private fun Difficulty.mapToDifficultyModel(): DifficultyModel {
        return DifficultyModel(
            id,
            name
        )
    }

    private fun DifficultyModel.mapToDifficulty(): Difficulty {
        return Difficulty(
            id,
            name
        )
    }
}