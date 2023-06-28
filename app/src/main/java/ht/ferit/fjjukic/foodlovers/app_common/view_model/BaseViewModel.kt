package ht.ferit.fjjukic.foodlovers.app_common.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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

abstract class BaseViewModel : ViewModel() {
    private var compositeDisposable: CompositeDisposable? = null

    protected val _actionNavigate = SingleLiveData<ActionNavigate>()
    protected val _showLoading = SingleLiveData<Boolean>() // TODO DEPRECATED BASEVIEWMODEL
    protected val _showMessage = SingleLiveData<MessageModel>() // TODO DEPRECATED BASEVIEWMODEL

    protected val _screenEvent = SingleLiveData<ScreenEvent>()
    val screenEvent: LiveData<ScreenEvent>
        get() = _screenEvent

    val actionNavigate: LiveData<ActionNavigate>
        get() = _actionNavigate

    protected fun handleError(value: String?) {
        showMessage(value, R.string.general_error_server)
    }

    protected fun showSnackbar(message: String? = null, messageId: Int? = null) {
        _screenEvent.postValue(SnackbarModel(message, messageId))
    }

    protected fun showMessage(message: String? = null, messageId: Int? = null) {
        _screenEvent.postValue(MessageModel(message, messageId))
    }


    protected suspend fun <T> handleResult(
        action: suspend () -> Result<T>,
        onSuccess: (T?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        _screenEvent.postValue(LoadingBar(true))
        val result = action.invoke()
        _screenEvent.postValue(LoadingBar(false))
        when {
            result.isSuccess -> {
                onSuccess(result.getOrNull())
            }

            result.isFailure -> {
                onError(result.exceptionOrNull())
            }

            else -> {}
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
            _showLoading.postValue(true)
        }.subscribe({
            _showLoading.postValue(false)
            onSuccess(it)
        }, {
            onError(it.message)
            _showLoading.postValue(false)
        }).addToDisposable()
    }
}