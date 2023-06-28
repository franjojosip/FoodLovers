package ht.ferit.fjjukic.foodlovers.app_common.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "username")
    var username: String,
    @ColumnInfo(name = "imagePath")
    var imagePath: String,
    @ColumnInfo(name = "latitude")
    var latitude: Double,
    @ColumnInfo(name = "longitude")
    var longitude: Double
) {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
}

