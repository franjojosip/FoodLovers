package ht.ferit.fjjukic.foodlovers.app_common.repository.food_type

import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.BaseModel
import io.reactivex.rxjava3.core.Observable

class FoodTypeRepositoryImpl(
    private val firebaseDB: FirebaseDB
) : FoodTypeRepository {

    override fun getFoodTypes(): Observable<List<BaseModel>> = firebaseDB.getFoodTypes()

    override fun insertFoodType(name: String): Observable<Boolean> = firebaseDB.postFoodType(name)

    override fun updateFoodType(oldName: String, newName: String): Observable<Boolean> =
        firebaseDB.putFoodType(oldName, newName)

    override fun deleteFoodType(foodTypeId: String): Observable<Boolean> =
        firebaseDB.deleteFoodType(foodTypeId)
}