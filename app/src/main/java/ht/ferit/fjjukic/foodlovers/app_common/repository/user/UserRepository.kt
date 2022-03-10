package ht.ferit.fjjukic.foodlovers.app_common.repository.user

import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import io.reactivex.rxjava3.core.Observable

interface UserRepository {
    fun insert(user: UserModel): Observable<Boolean>

    fun get(userId: String): Observable<UserModel>

    fun update(user: UserModel): Observable<Boolean>

    fun delete(userId: String): Observable<Boolean>
}