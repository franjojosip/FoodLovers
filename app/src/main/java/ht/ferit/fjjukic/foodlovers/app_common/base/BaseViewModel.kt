package ht.ferit.fjjukic.foodlovers.app_common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.live_data.SingleLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ScreenEvent
import ht.ferit.fjjukic.foodlovers.app_common.model.SnackbarModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val analyticsProvider: AnalyticsProvider
) : ViewModel() {
    val actionNavigate = SingleLiveData<ActionNavigate>()
    val screenEvent = SingleLiveData<ScreenEvent>()
    val messageScreenEvent = SingleLiveData<ScreenEvent>()

    fun logScreenEvent(screenName: String, additionalInfo: Pair<String, String>?) {
        analyticsProvider.logScreenEvent(screenName, additionalInfo)
    }

    protected fun showSnackbar(message: String? = null, messageId: Int? = null) {
        messageScreenEvent.postValue(SnackbarModel(message, messageId))
    }

    protected fun showMessage(message: String? = null, messageId: Int? = null) {
        messageScreenEvent.postValue(MessageModel(message, messageId))
    }

    protected fun <T> handleResult(
        action: suspend () -> Result<T>,
        onSuccess: suspend (T?) -> Unit,
        onError: (Throwable?) -> Unit,
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
        showLoading: Boolean = true
    ) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                screenEvent.postValue(LoadingBar(showLoading))
                val result = action.invoke()

                when {
                    result.isSuccess -> {
                        onSuccess(result.getOrNull())
                        screenEvent.postValue(LoadingBar(false))
                    }

                    result.isFailure -> {
                        onError(result.exceptionOrNull())
                        screenEvent.postValue(LoadingBar(false))
                    }

                    else -> {
                        screenEvent.postValue(LoadingBar(false))
                    }
                }
            } catch (e: Exception) {
                screenEvent.postValue(LoadingBar(false))
            }

        }
    }
}