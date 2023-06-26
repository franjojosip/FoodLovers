package ht.ferit.fjjukic.foodlovers.app_common.repository.user

import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRepositoryImpl(
    private val firebaseDB: FirebaseDB,
    private val preferenceManager: PreferenceManager,
    private val firebaseSource: FirebaseSource
) : UserRepository {
    override fun insert(user: UserModel): Observable<Boolean> {
        return Observable.just(true)
    }

    override suspend fun get(userId: String): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            val user = preferenceManager.user
            when {
                user != null -> {
                    return@withContext Result.success(user)
                }

                else -> {
                    val firebaseUser = firebaseDB.getUser(userId)
                        ?: return@withContext Result.failure(Exception("test"))
                    preferenceManager.user = firebaseUser
                    return@withContext Result.success(firebaseUser)
                }
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