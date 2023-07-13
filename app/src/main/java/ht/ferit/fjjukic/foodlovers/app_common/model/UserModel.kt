package ht.ferit.fjjukic.foodlovers.app_common.model

import ht.ferit.fjjukic.foodlovers.app_common.constants.Constants

class UserModel(
    var userId: String = "",
    var name: String = "",
    var email: String = "",
    var imageUrl: String = "",
    val admin: Boolean = false,
    var latitude: String = Constants.DEFAULT_LAT,
    var longitude: String = Constants.DEFAULT_LONG
)