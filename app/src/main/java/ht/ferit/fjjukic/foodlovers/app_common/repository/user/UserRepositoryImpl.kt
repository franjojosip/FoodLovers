package ht.ferit.fjjukic.foodlovers.app_common.repository.user

import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import io.reactivex.rxjava3.core.Observable


class UserRepositoryImpl(
    private val firebaseDB: FirebaseDB,
    private val preferenceManager: PreferenceManager
) : UserRepository {
    override fun insert(user: UserModel): Observable<Boolean> {
        return firebaseDB.postUser(user).map { isSuccess ->
            if (isSuccess) {
                preferenceManager.user = user
            }
            isSuccess
        }
    }

    override fun get(userId: String): Observable<UserModel> {
        val user = preferenceManager.user
        return when {
            user != null -> {
                Observable.just(user)
            }
            else -> firebaseDB.getUser(userId).map {
                preferenceManager.user = it
                it
            }
        }
    }

    override fun update(user: UserModel): Observable<Boolean> {
        return firebaseDB.putUser(user).map { isSuccess ->
            if (isSuccess) {
                preferenceManager.user = user
            }
            isSuccess
        }
    }

    override fun delete(userId: String): Observable<Boolean> {
        return firebaseDB.deleteUser(userId).map { isSuccess ->
            if (isSuccess) {
                preferenceManager.user = null
            }
            isSuccess
        }
    }

}