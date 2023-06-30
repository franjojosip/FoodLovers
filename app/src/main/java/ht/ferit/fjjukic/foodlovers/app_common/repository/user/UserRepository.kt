package ht.ferit.fjjukic.foodlovers.app_common.repository.user

import com.google.firebase.auth.FirebaseUser
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel

interface UserRepository {
    suspend fun createUser(user: UserModel): Result<UserModel?>
    suspend fun getUser(userId: String, isForRecipe: Boolean = false): Result<UserModel>
    suspend fun updateUser(user: UserModel): Result<Boolean>
    suspend fun deleteUser(userId: String): Result<Boolean>

    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun register(email: String, username: String, password: String): Result<Boolean>
    suspend fun resetPassword(email: String): Result<Boolean>
    suspend fun reauthenticateUser(email: String, password: String): Result<Boolean>

    suspend fun updateEmail(email: String): Result<Boolean>
    suspend fun logout(): Result<Boolean>
    fun currentUser(): FirebaseUser?
}