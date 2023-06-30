package ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty

import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel

interface DifficultyRepository {
    suspend fun getDifficulty(id: String): Result<DifficultyModel?>
    suspend fun getDifficulties(): Result<List<DifficultyModel>>
    suspend fun createDifficulty(difficulty: DifficultyModel): Result<DifficultyModel>
    suspend fun updateDifficulty(difficulty: DifficultyModel): Result<DifficultyModel>
    suspend fun deleteDifficulty(id: String): Result<Boolean>
}