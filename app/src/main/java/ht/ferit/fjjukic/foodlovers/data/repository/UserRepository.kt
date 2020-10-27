package ht.ferit.fjjukic.foodlovers.data.repository

import ht.ferit.fjjukic.foodlovers.data.database.FirebaseDB
import ht.ferit.fjjukic.foodlovers.data.model.UserModel
import ht.ferit.fjjukic.foodlovers.ui.common.FirebaseDatabaseCallback

class UserRepository(
    private val firebaseDB: FirebaseDB
) {
    fun insert(user: UserModel) = firebaseDB.postUser(user)

    fun get(userId: String, callback: FirebaseDatabaseCallback) =
        firebaseDB.getUser(userId, callback)

    fun update(user: UserModel) = firebaseDB.putUser(user)

    fun delete(userId: String) = firebaseDB.deleteUser(userId)
}