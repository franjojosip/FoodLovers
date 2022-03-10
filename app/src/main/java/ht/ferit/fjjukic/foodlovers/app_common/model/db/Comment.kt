package ht.ferit.fjjukic.foodlovers.app_common.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comment",
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("recipeID"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Comment(
    @ColumnInfo(name = "comment")
    val comment: String,
    @ColumnInfo(name = "image_path")
    val imagePath: String,
    @ColumnInfo(name = "recipeID", index = true)
    val recipeID: Int
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
