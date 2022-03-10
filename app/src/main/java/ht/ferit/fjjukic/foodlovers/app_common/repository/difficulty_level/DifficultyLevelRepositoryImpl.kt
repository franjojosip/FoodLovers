package ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty_level

import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.BaseModel
import io.reactivex.rxjava3.core.Observable

class DifficultyLevelRepositoryImpl(private val firebaseDB: FirebaseDB): DifficultyLevelRepository {

    override fun getDifficultyLevels(): Observable<List<BaseModel>> =
        firebaseDB.getDifficultyLevels()

    override fun insertDifficultyLevel(name: String): Observable<Boolean> =
        firebaseDB.postDifficultyLevel(name)

    override fun updateDifficultyLevel(oldName: String, newName: String): Observable<Boolean> =
        firebaseDB.putDifficultyLevel(oldName, newName)

    override fun deleteDifficultyLevel(foodTypeId: String): Observable<Boolean> =
        firebaseDB.deleteDifficultyLevel(foodTypeId)
}