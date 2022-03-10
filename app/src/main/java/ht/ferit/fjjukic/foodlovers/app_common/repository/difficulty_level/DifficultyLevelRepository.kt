package ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty_level

import ht.ferit.fjjukic.foodlovers.app_common.model.BaseModel
import io.reactivex.rxjava3.core.Observable

interface DifficultyLevelRepository {
    fun getDifficultyLevels(): Observable<List<BaseModel>>

    fun insertDifficultyLevel(name: String): Observable<Boolean>

    fun updateDifficultyLevel(oldName: String, newName: String): Observable<Boolean>

    fun deleteDifficultyLevel(foodTypeId: String): Observable<Boolean>
}