package ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty

import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel

interface DifficultyRepository {
    suspend fun getDifficulty(id: String): Result<DifficultyModel?>
    suspend fun getDifficulties(): Result<List<DifficultyModel>>
    suspend fun updateDifficulty(category: DifficultyModel): Result<Boolean>
    suspend fun createDifficulty(category: DifficultyModel): Result<Boolean>
    suspend fun deleteDifficulty(id: String): Result<Boolean>
}