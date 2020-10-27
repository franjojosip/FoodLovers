package ht.ferit.fjjukic.foodlovers.data

import android.content.Context
import ht.ferit.fjjukic.foodlovers.FirebaseApplication

class PreferenceManager {
    companion object {
        const val PREFS_FILE = "ids"
        const val PREFS_KEY_FOOD_TYPE = "foodTypeID"
        const val PREFS_KEY_DIFFICULTY_LEVEL = "difficultyLevelID"
    }

    private val sharedPreferences = FirebaseApplication.ApplicationContext.getSharedPreferences(
        PREFS_FILE, Context.MODE_PRIVATE
    )

    fun setFoodTypeID(id: String) {
        val editor = this.sharedPreferences.edit()
        editor.putString(PREFS_KEY_FOOD_TYPE, id)
        editor.apply()
    }

    fun getFoodTypeID(): String {
        return this.sharedPreferences.getString(PREFS_KEY_FOOD_TYPE, "0").toString()
    }

    fun setDifficultyLevelID(id: String) {
        val editor = this.sharedPreferences.edit()
        editor.putString(PREFS_KEY_DIFFICULTY_LEVEL, id)
        editor.apply()
    }

    fun getDifficultyLevelID(): String {
        return this.sharedPreferences.getString(PREFS_KEY_DIFFICULTY_LEVEL, "0").toString()
    }

}