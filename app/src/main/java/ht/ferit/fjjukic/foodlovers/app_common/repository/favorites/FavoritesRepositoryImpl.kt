package ht.ferit.fjjukic.foodlovers.app_common.repository.favorites

import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoritesRepositoryImpl(
    private val firebaseDB: FirebaseDB
) : FavoritesRepository {
    override suspend fun saveFavorites() {
        withContext(Dispatchers.IO) {
            firebaseDB.saveFavorites()
        }
    }

    override suspend fun loadFavorites() {
        return withContext(Dispatchers.IO) {
            firebaseDB.loadFavorites()
        }
    }

}