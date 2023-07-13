package ht.ferit.fjjukic.foodlovers.app_common.repository.favorites

interface FavoritesRepository {
    suspend fun saveFavorites()
    suspend fun loadFavorites()
}