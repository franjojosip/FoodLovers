package ht.ferit.fjjukic.foodlovers.data.repository

import androidx.lifecycle.LiveData
import ht.ferit.fjjukic.foodlovers.data.database.UserDao
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.data.model.User

class UserRepository(
    private val firebase: FirebaseSource,
    private val userDao: UserDao
) {

    fun register(email: String, password: String) = firebase.register(email, password)

    fun login(email: String, password: String) = firebase.login(email, password)

    fun currentUser() = firebase.currentUser()

    fun resetPassword(email: String) = firebase.resetPassword(email)

    fun logout() = firebase.logout()

    fun insert(user: User) = userDao.insert(user)

    fun get(email: String): LiveData<User> {
        return userDao.get(email)
    }

    fun getAll(): LiveData<List<User>> {
        return userDao.getAll()
    }

    fun update(user: User) = userDao.update(user)

    fun deleteAll() = userDao.deleteAll()
}