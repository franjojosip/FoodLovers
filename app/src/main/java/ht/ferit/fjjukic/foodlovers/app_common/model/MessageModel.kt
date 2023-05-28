package ht.ferit.fjjukic.foodlovers.app_common.model


sealed class ScreenEvent

object ActionBack: ScreenEvent()

class LoadingBar(val isVisible: Boolean): ScreenEvent()

data class MessageModel(
    val message: String? = null,
    val messageId: Int? = null
): ScreenEvent()