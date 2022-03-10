package ht.ferit.fjjukic.foodlovers.app_common.repository.resource

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes

class ResourceRepositoryImpl(private val context: Context) : ResourceRepository {
    override fun getResources(): Resources = context.resources

    override fun getString(@StringRes id: Int): String = context.resources.getString(id)

    override fun getFormattedString(@StringRes id: Int, params: Any): String =
        context.resources.getString(id, params)
}