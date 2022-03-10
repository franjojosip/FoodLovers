package ht.ferit.fjjukic.foodlovers.app_common.repository.food_type

import ht.ferit.fjjukic.foodlovers.app_common.model.BaseModel
import io.reactivex.rxjava3.core.Observable

interface FoodTypeRepository {

    fun getFoodTypes(): Observable<List<BaseModel>>

    fun insertFoodType(name: String): Observable<Boolean>

    fun updateFoodType(oldName: String, newName: String): Observable<Boolean>

    fun deleteFoodType(foodTypeId: String): Observable<Boolean>
}