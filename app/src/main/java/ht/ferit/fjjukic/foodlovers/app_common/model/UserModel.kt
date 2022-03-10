package ht.ferit.fjjukic.foodlovers.app_common.model

import ht.ferit.fjjukic.foodlovers.app_common.constants.Constants

class UserModel(
    var id: String = "",
    var userId: String = "",
    var name: String = "",
    var email: String = "",
    var imageUrl: String = "",
    var latitude: String = Constants.DEFAULT_LAT,
    var longitude: String = Constants.DEFAULT_LONG
)