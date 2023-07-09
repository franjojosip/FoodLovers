package ht.ferit.fjjukic.foodlovers.app_common.repository.user

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NoRecipePlaceholder.user
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val firebaseStorage: FirebaseStorage,
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

    override suspend fun getUser(userId: String, isForRecipe: Boolean): Result<UserModel> {
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
                preferenceManager.user = user
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

    override suspend fun updateUserImage(uri: Uri): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            val user = preferenceManager.user
            if (user != null){
                user.imageUrl = saveUserImage(uri, user.userId)
                    ?: return@withContext Result.failure(Exception("Error while saving image"))

                val result = firebaseDB.updateUser(user)

                if (result.isFailure) {
                    result
                } else {
                    preferenceManager.user = user
                    Result.success(true)
                }
            }
            else {
                Result.failure(Exception("Error invalid user"))
            }
        }
    }

    private suspend fun saveUserImage(value: Uri, id: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val ref = firebaseStorage.reference.child(getImagePath(id))
                val uploadTask = ref.putFile(value).await()

                when {
                    uploadTask.error != null -> {
                        null
                    }

                    else -> {
                        uploadTask.storage.downloadUrl.await().toString()
                    }
                }
            } catch (e: Exception) {
                Log.d("log", e.message.toString())
                null
            }
        }
    }

    private fun getImagePath(id: String) = "images/$id.jpg"

    override suspend fun login(email: String, password: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.fetchSignInMethodsForEmail(email).await()
                    ?: return@withContext Result.failure(Exception("FirebaseAuth error - email doesn't exist"))

                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val userId = result?.user?.uid
                    ?: return@withContext Result.failure(Exception("FirebaseAuth error - sign in failed"))

                preferenceManager.user =
                    getUser(userId).getOrNull() ?: return@withContext Result.failure(
                        Exception("FirebaseAuth error - user doesn't exist")
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

    override suspend fun updateEmail(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val user = preferenceManager.user
                val emailExists = checkEmailExist(email)
                if (emailExists.isSuccess || user == null) {
                    Result.success(false)
                } else {
                    user.email = email
                    updateUser(user)
                    firebaseAuth.currentUser?.updateEmail(email)?.await()
                    Result.success(true)
                }
            } catch (e: Exception) {
                Result.failure(Exception("FirebaseAuth server error"))
            }
        }
    }

    private suspend fun checkEmailExist(email: String): Result<Boolean> {
        return firebaseDB.checkEmailExist(email)
    }

    override suspend fun logout(): Result<Boolean> {
        firebaseAuth.signOut()
        preferenceManager.user = null

        return Result.success(true)
    }

    override fun currentUser() = firebaseAuth.currentUser
}