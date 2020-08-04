package ht.ferit.fjjukic.foodlovers.data.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "recipe",
    indices = [Index("difficultyLevelID"), Index("foodTypeID")],
    foreignKeys = [ForeignKey(
        entity = DifficultyLevel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("difficultyLevelID"),
        onDelete = CASCADE,
        onUpdate = CASCADE
    ),
        ForeignKey(
            entity = FoodType::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("foodTypeID"),
            onDelete = CASCADE,
            onUpdate = CASCADE
        )]
)
data class Recipe(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "image_path")
    var imagePath: String,
    @ColumnInfo(name = "difficultyLevelID")
    var difficultyLevelID: Int,
    @ColumnInfo(name = "foodTypeID")
    var typeID: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

