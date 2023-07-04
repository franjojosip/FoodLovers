package ht.ferit.fjjukic.foodlovers.app_common.repository.filters

import ht.ferit.fjjukic.foodlovers.app_common.model.FilterModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.category.CategoryRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.difficulty.DifficultyRepository
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilterRepositoryImpl(
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository,
) : FilterRepository {

    override suspend fun getFilterModel(): FilterModel {
        return withContext(Dispatchers.IO) {
            val filterCategories = categoryRepository.getFilterCategories()
            val filterDifficulties = difficultyRepository.getFilterDifficulties()
            val filterTimes = getFilterTimes()
            val filterSorts = getFilterSorts()

            FilterModel(
                filterCategories,
                filterDifficulties,
                filterTimes,
                filterSorts
            )
        }
    }

    private fun getFilterTimes(): List<FilterItem.Time> {
        return listOf(
            FilterItem.Time("All", 5000, isChecked = true, isDefault = true),
            FilterItem.Time("15min", 15, isChecked = false, isDefault = false),
            FilterItem.Time("30min", 30, isChecked = false, isDefault = false),
            FilterItem.Time("45min", 45, isChecked = false, isDefault = false),
            FilterItem.Time("1h", 60, isChecked = false, isDefault = false),
            FilterItem.Time("1h30min", 90, isChecked = false, isDefault = false),
            FilterItem.Time("2h", 120, isChecked = false, isDefault = false),
            FilterItem.Time("2h30min", 150, isChecked = false, isDefault = false),
            FilterItem.Time("3h", 180, isChecked = false, isDefault = false),
        )
    }

    private fun getFilterSorts(): List<FilterItem.Sort> {
        return listOf(
            FilterItem.Sort("A-Z", isAsc = true, isChecked = true, isDefault = true),
            FilterItem.Sort("Z-A", isAsc = true, isChecked = false, isDefault = false)
        )
    }
}