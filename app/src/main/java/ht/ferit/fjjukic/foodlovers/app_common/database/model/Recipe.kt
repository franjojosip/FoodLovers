package ht.ferit.fjjukic.foodlovers.app_common.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "recipe",
    indices = [Index("difficultyId"), Index("categoryId")],
    foreignKeys = [ForeignKey(
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
        )]
)
data class Recipe(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "servings")
    var servings: Int,
    @ColumnInfo(name = "difficultyId")
    var difficultyId: String,
    @ColumnInfo(name = "categoryId")
    var categoryId: String,
    @ColumnInfo(name = "image_path")
    var imagePath: String,
    @ColumnInfo(name = "userId")
    var userId: String,
) {
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
}

