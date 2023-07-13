package ht.ferit.fjjukic.foodlovers.app_common.repository.filters

import ht.ferit.fjjukic.foodlovers.app_common.model.FilterModel

interface FilterRepository {
    suspend fun getFilterModel(): FilterModel
}