package ht.ferit.fjjukic.foodlovers.app_common.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel

class PreferenceManagerImpl(applicationContext: Context, private val gson: Gson) :
    PreferenceManager {

    private val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
        PREFS_FILE, Context.MODE_PRIVATE
    )

    override var user: UserModel?
        get() = gson.fromJson(
            sharedPreferences.getString(PREFS_KEY_USER, null),
            UserModel::class.java
        )
        set(value) {
            editor { putString(PREFS_KEY_USER, gson.toJson(value)) }
        }
    override var userId: String?
        get() = sharedPreferences.getString(PREFS_KEY_USER_ID, null)
        set(value) {
            editor { putString(PREFS_KEY_USER_ID, value) }
        }
    override var isFirstTime: Boolean
        get() = sharedPreferences.getBoolean(PREFS_KEY_IS_FIRST_TIME, true)
        set(value) {
            editor { putBoolean(PREFS_KEY_IS_FIRST_TIME, value) }
        }
    override var lastUpdatedRecipes: Long
        get() = sharedPreferences.getLong(PREFS_KEY_LAST_UPDATED_RECIPES, 0)
        set(value) {
            editor { putLong(PREFS_KEY_LAST_UPDATED_RECIPES, value) }
        }
    override var lastUpdatedCategories: Long
        get() = sharedPreferences.getLong(PREFS_KEY_LAST_UPDATED_CATEGORIES, 0)
        set(value) {
            editor { putLong(PREFS_KEY_LAST_UPDATED_CATEGORIES, value) }
        }
    override var lastUpdatedDifficulties: Long
        get() = sharedPreferences.getLong(PREFS_KEY_LAST_UPDATED_DIFFICULTIES, 0)
        set(value) {
            editor { putLong(PREFS_KEY_LAST_UPDATED_DIFFICULTIES, value) }
        }

    private fun editor(editor: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences.edit().also(editor).apply()
    }
}