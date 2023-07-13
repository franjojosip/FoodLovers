package ht.ferit.fjjukic.foodlovers.app_common.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import ht.ferit.fjjukic.foodlovers.app_common.database.parser.JsonParser
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Ingredient
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Step

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {

    @TypeConverter
    fun fromStepsJson(json: String): List<Step> {
        return jsonParser.fromJson<ArrayList<Step>>(
            json,
            object : TypeToken<ArrayList<Step>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toStepsJson(data: List<Step>): String {
        return jsonParser.toJson(
            data,
            object : TypeToken<List<Step>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromIngredientsJson(json: String): List<Ingredient> {
        return jsonParser.fromJson<ArrayList<Ingredient>>(
            json,
            object : TypeToken<ArrayList<Ingredient>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toIngredientsJson(data: List<Ingredient>): String {
        return jsonParser.toJson(
            data,
            object : TypeToken<List<Ingredient>>() {}.type
        ) ?: "[]"
    }
}