package ht.ferit.fjjukic.foodlovers.app_common.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.live_data.SingleLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ScreenEvent
import ht.ferit.fjjukic.foodlovers.app_common.model.SnackbarModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private var compositeDisposable: CompositeDisposable? = null

    protected val _actionNavigate = SingleLiveData<ActionNavigate>()
    protected val _screenEvent = SingleLiveData<ScreenEvent>()

    val screenEvent: LiveData<ScreenEvent>
        get() = _screenEvent

    val actionNavigate: LiveData<ActionNavigate>
        get() = _actionNavigate

    protected fun handleError(value: String?, isToast: Boolean = true) {
        if (isToast) {
            showMessage(value, R.string.general_error_server)
        } else showSnackbar(value, R.string.general_error_server)
    }

    protected fun showSnackbar(message: String? = null, messageId: Int? = null) {
        _screenEvent.postValue(SnackbarModel(message, messageId))
    }

    protected fun showMessage(message: String? = null, messageId: Int? = null) {
        _screenEvent.postValue(MessageModel(message, messageId))
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
                _screenEvent.postValue(LoadingBar(showLoading))
                delay(200)
                val result = action.invoke()

                when {
                    result.isSuccess -> {
                        onSuccess(result.getOrNull())
                        _screenEvent.postValue(LoadingBar(false))
                    }

                    result.isFailure -> {
                        onError(result.exceptionOrNull())
                        _screenEvent.postValue(LoadingBar(false))
                    }

                    else -> _screenEvent.postValue(LoadingBar(false))
                }
            } catch (e: Exception) {
                _screenEvent.postValue(LoadingBar(false))
            }

        }
    }

    fun Disposable.addToDisposable() {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.dispose()
    }

    fun <T : Any> Observable<T>.subscribeIO(): Observable<T> = this.subscribeOn(Schedulers.io())

    fun <T : Any> Observable<T>.observeMain(): Observable<T> =
        this.observeOn(AndroidSchedulers.mainThread())

    fun <T : Any> Observable<T>.subscribeWithResult(
        onSuccess: (T) -> Unit,
        onError: (String?) -> Unit
    ) {
        return this.doOnSubscribe {
            _screenEvent.postValue(LoadingBar(true))
        }.subscribe({
            _screenEvent.postValue(LoadingBar(false))
            onSuccess(it)
        }, {
            onError(it.message)
            _screenEvent.postValue(LoadingBar(false))
        }).addToDisposable()
    }
}