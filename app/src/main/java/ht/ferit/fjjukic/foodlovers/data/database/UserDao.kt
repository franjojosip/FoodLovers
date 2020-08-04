package ht.ferit.fjjukic.foodlovers.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import ht.ferit.fjjukic.foodlovers.data.model.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE email = :email")
    fun get(email: String): LiveData<User>

    @Update
    fun update(user: User)

    @Query("DELETE FROM user WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM user")
    fun deleteAll()
}