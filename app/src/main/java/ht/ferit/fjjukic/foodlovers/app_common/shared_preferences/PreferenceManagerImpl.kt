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

    private fun editor(editor: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences.edit().also(editor).apply()
    }
}