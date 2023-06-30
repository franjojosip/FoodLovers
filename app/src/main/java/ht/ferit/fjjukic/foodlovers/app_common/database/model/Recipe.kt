package ht.ferit.fjjukic.foodlovers.app_common.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Ingredient
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Step

@Entity(
    tableName = "recipe",
    foreignKeys = [
        ForeignKey(
            entity = Difficulty::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("difficultyId"),
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)

data class Recipe(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "time")
    var time: Int,
    @ColumnInfo(name = "servings")
    var servings: Int,
    @ColumnInfo(name = "steps")
    var steps: List<Step>,
    @ColumnInfo(name = "ingredients")
    var ingredients: List<Ingredient>,
    @ColumnInfo(name = "difficultyId")
    var difficultyId: String,
    @ColumnInfo(name = "categoryId")
    var categoryId: String,
    @ColumnInfo(name = "image_path")
    var imagePath: String,
    @ColumnInfo(name = "userId")
    var userId: String,
)

