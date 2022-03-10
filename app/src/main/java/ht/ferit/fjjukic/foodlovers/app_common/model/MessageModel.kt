package ht.ferit.fjjukic.foodlovers.app_common.model


sealed class ScreenEvent

data class MessageModel(
    val message: String? = null,
    val messageId: Int? = null
): ScreenEvent()