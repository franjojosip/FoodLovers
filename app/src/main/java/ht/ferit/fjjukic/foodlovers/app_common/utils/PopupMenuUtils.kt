package ht.ferit.fjjukic.foodlovers.app_common.utils

import android.content.Context
import android.view.Menu
import android.widget.PopupMenu
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.BaseModel

fun PopupMenu.populateMenu(context: Context, data: List<BaseModel>): PopupMenu {
    this.menu.apply {
        clear()
        add(Menu.NONE, 0, 0, context.getString(R.string.option_all))
        data.forEachIndexed { index, item ->
            add(Menu.NONE, index + 1, index + 1, item.name)
        }
    }
    return this
}