package ht.ferit.fjjukic.foodlovers.app_common.repository.user

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDB: FirebaseDB,
    private val preferenceManager: PreferenceManager
) : UserRepository {

    override suspend fun createUser(user: UserModel): Result<UserModel?> {
        return withContext(Dispatchers.IO) {
            val result = firebaseDB.createUser(user)
            preferenceManager.user = result.getOrNull()

            return@withContext if (result.isSuccess) {
                Result.success(result.getOrDefault(null))
            } else Result.failure(result.exceptionOrNull() ?: Exception("User repository error"))
        }
    }

    override suspend fun getUser(userId: String): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            var user = preferenceManager.user
            when {
                user != null -> {
                    return@withContext Result.success(user)
                }

                else -> {
                    user =
                        firebaseDB.getUser(userId).getOrNull() ?: return@withContext Result.failure(
                            Exception("User repository error")
                        )
                    preferenceManager.user = user
                    return@withContext Result.success(user)
                }
            }
        }
    }

    override suspend fun updateUser(user: UserModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            val result = firebaseDB.updateUser(user)
            return@withContext if (result.isSuccess) {
                Result.success(result.getOrDefault(false))
            } else Result.failure(result.exceptionOrNull() ?: Exception("User repository error"))
        }
    }

    override suspend fun deleteUser(userId: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            val result = firebaseDB.deleteUser(userId)
            return@withContext if (result.isSuccess) {
                Result.success(result.getOrDefault(false))
            } else Result.failure(result.exceptionOrNull() ?: Exception("User repository error"))
        }
    }


    override suspend fun login(email: String, password: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.fetchSignInMethodsForEmail(email).await()
                    ?: return@withContext Result.failure(Exception("FirebaseAuth error - fetchSignInMethodsForEmail"))

                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val userId = result?.user?.uid
                    ?: return@withContext Result.failure(Exception("FirebaseAuth error - signInWithEmailAndPassword"))

                preferenceManager.user =
                    getUser(userId).getOrNull() ?: return@withContext Result.failure(
                        Exception("FirebaseAuth error - user not retrieved")
                    )

                return@withContext Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("FirebaseAuth server error"))
            }
        }
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

                val userId = result?.user?.uid
                    ?: return@withContext Result.failure(Exception("FirebaseAuth error - user not retrieved"))

                val userModel = UserModel(
                    userId = userId,
                    name = username,
                    email = email
                )

                createUser(userModel)

                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("FirebaseAuth server error"))
            }
        }
    }

    override suspend fun resetPassword(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                Result.success(true)

            } catch (e: Exception) {
                Result.failure(Exception("FirebaseAuth server error"))
            }
        }
    }

    override suspend fun reauthenticateUser(email: String, password: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.currentUser?.reauthenticate(
                    EmailAuthProvider.getCredential(
                        email,
                        password
                    )
                )
                    ?.await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("FirebaseAuth server error"))
            }
        }
    }

    override suspend fun updateEmail(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.currentUser?.updateEmail(email)?.await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("FirebaseAuth server error"))
            }
        }
    }

    override suspend fun logout(): Result<Boolean> {
        firebaseAuth.signOut()
        preferenceManager.user = null

        return Result.success(true)
    }

    override fun currentUser() = firebaseAuth.currentUser
}