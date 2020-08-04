package ht.ferit.fjjukic.foodlovers.data.repository

import androidx.lifecycle.LiveData
import ht.ferit.fjjukic.foodlovers.data.database.DifficultyLevelDao
import ht.ferit.fjjukic.foodlovers.data.model.DifficultyLevel

class DifficultyLevelRepository(
    private val difficultyLevelDao: DifficultyLevelDao
) {

    fun get(id: Int): LiveData<DifficultyLevel> {
        return difficultyLevelDao.get(id)
    }

    fun getAll(): LiveData<List<DifficultyLevel>> {
        return difficultyLevelDao.getAll()
    }

    fun insert(difficultyLevel: DifficultyLevel) {
        difficultyLevelDao.insert(difficultyLevel)
    }

    fun insertAll(difficultyLevels: List<DifficultyLevel>) {
        difficultyLevelDao.insertAll(difficultyLevels)
    }

    fun update(difficultyLevel: DifficultyLevel) {
        difficultyLevelDao.update(difficultyLevel)
    }

    fun delete(id:Int) {
        difficultyLevelDao.delete(id)
    }

    fun deleteAll() {
        difficultyLevelDao.deleteAll()
    }
}