package ht.ferit.fjjukic.foodlovers.app_common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.live_data.SingleLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ScreenEvent
import ht.ferit.fjjukic.foodlovers.app_common.model.SnackbarModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    val actionNavigate = SingleLiveData<ActionNavigate>()
    val screenEvent = SingleLiveData<ScreenEvent>()
    val messageScreenEvent = SingleLiveData<ScreenEvent>()

    protected fun handleError(value: String?, isToast: Boolean = true) {
        if (isToast) {
            showMessage(value, R.string.general_error_server)
        } else showSnackbar(value, R.string.general_error_server)
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

                    else -> screenEvent.postValue(LoadingBar(false))
                }
            } catch (e: Exception) {
                screenEvent.postValue(LoadingBar(false))
            }

        }
    }
}