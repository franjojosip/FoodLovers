package ht.ferit.fjjukic.foodlovers.data

import android.content.Context
import ht.ferit.fjjukic.foodlovers.FirebaseApplication

class PreferenceManager {
    companion object {
        const val PREFS_FILE = "foodType"
        const val PREFS_KEY_FOOD_TYPE = "foodTypeID"
    }
    private val sharedPreferences = FirebaseApplication.ApplicationContext.getSharedPreferences(
        PREFS_FILE, Context.MODE_PRIVATE
    )

    fun setFoodTypeID(id: Int) {
        val editor = this.sharedPreferences.edit()
        editor.putInt(PREFS_KEY_FOOD_TYPE, id)
        editor.apply()
    }

    fun getFoodTypeID(): Int {
        return this.sharedPreferences.getInt(PREFS_KEY_FOOD_TYPE, 0)
    }
}