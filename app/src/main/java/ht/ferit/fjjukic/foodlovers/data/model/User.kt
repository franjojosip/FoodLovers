package ht.ferit.fjjukic.foodlovers.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

