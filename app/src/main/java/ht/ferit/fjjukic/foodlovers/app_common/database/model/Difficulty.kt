package ht.ferit.fjjukic.foodlovers.app_common.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "difficulty")
data class Difficulty(
    @ColumnInfo(name = "name")
    var name: String
) {
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
}
