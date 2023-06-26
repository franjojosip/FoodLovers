package ht.ferit.fjjukic.foodlovers.app_common.firebase

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseSource(
    private val preferenceManager: PreferenceManager,
    private val firebaseDB: FirebaseDB
) {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    suspend fun login(email: String, password: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val userId = result?.user?.uid
                    ?: return@withContext Result.failure(Exception("Error while working with FirebaseAuth"))
                preferenceManager.userId = userId
                preferenceManager.user = firebaseDB.getUser(userId)

                return@withContext Result.success(true)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    suspend fun register(email: String, username: String, password: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

                val userId = result?.user?.uid
                    ?: return@withContext Result.failure(Exception("Error while working with FirebaseAuth"))

                val userModel = UserModel(
                    userId = userId,
                    name = username,
                    email = email
                )

                firebaseDB.createUser(userModel)
                preferenceManager.user = userModel
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Throwable(e))
            }
        }
    }

    suspend fun resetPassword(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                Result.success(true)

            } catch (e: Exception) {
                Result.failure(Throwable(e))
            }
        }
    }

    fun reauthenticateUser(
        firebaseUser: FirebaseUser,
        email: String,
        password: String
    ): Observable<Boolean> {
        return Observable.create { emitter ->
            firebaseUser.reauthenticate(EmailAuthProvider.getCredential(email, password))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        emitter.onNext(true)
                    } else emitter.onError(Throwable(task.exception?.message))
                }
        }
    }

    fun updateEmail(
        firebaseUser: FirebaseUser,
        email: String
    ): Observable<Boolean> {
        return Observable.create { emitter ->
            firebaseUser.updateEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onNext(true)
                } else emitter.onError(Throwable(task.exception?.message))
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        preferenceManager.user = null
    }

    fun currentUser() = firebaseAuth.currentUser

}