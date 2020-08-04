package ht.ferit.fjjukic.foodlovers.data.model

import androidx.room.*

@Entity(
    tableName = "comment",
    foreignKeys = [ForeignKey(entity = Recipe::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("recipeID"),
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
)]
)
data class Comment(
    @ColumnInfo(name="comment")
    val comment: String,
    @ColumnInfo(name="image_path")
    val imagePath: String,
    @ColumnInfo(name="recipeID", index = true)
    val recipeID: Int
){
    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
