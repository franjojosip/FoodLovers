package ht.ferit.fjjukic.foodlovers.app_common.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "difficulty_level")
data class DifficultyLevel(
    @ColumnInfo(name = "name")
    var name: String
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
