package ht.ferit.fjjukic.foodlovers.app_common.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding4.widget.textChanges
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionBack
import ht.ferit.fjjukic.foodlovers.app_common.model.DialogModel
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.showAlertDialog
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseFragment<VM : BaseViewModel, ViewBinding : ViewDataBinding> : Fragment() {
    protected abstract val layoutId: Int
    protected abstract val viewModel: VM
    protected lateinit var binding: ViewBinding
    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        init()
    }

    open fun setObservers() {
        viewModel.screenEvent.observe(viewLifecycleOwner) {
            when (it) {
                is MessageModel -> showToast(it)
                is DialogModel -> context?.showAlertDialog(it)
                is LoadingBar -> {
                    (binding.root.findViewById(R.id.loader_layout) as? View)?.isVisible =
                        it.isVisible
                }
                ActionBack -> activity?.onBackPressed()
            }
        }
    }

    abstract fun init()

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun addDisposable(disposable: Disposable) {
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable.addAll(disposable)
    }

    protected fun showToast(
        message: String? = "",
        messageId: Int? = null,
        length: Int = Toast.LENGTH_SHORT
    ) {
        when {
            !message.isNullOrEmpty() -> {
                Toast.makeText(context, message, length).show()
            }
            messageId != null -> {
                Toast.makeText(context, getString(messageId), length).show()
            }
        }
    }

    protected fun showToast(
        model: MessageModel,
        length: Int = Toast.LENGTH_SHORT
    ) {
        when {
            !model.message.isNullOrEmpty() -> {
                Toast.makeText(context, model.message, length).show()
            }
            model.messageId != null -> {
                Toast.makeText(context, getString(model.messageId), length).show()
            }
        }
    }

    protected fun <T : Any> Observable<T>.subscribeToView(onNext: (T) -> Unit = {}) = addDisposable(
        this.observeOn(AndroidSchedulers.mainThread()).subscribe(onNext) {
            it.printStackTrace()
        })

    private fun Disposable.addToDisposable() {
        compositeDisposable.add(this)
    }

    protected fun EditText.subscribeTextChanges(action: (String) -> Unit) =
        this.textChanges().skipInitialValue().map(
            CharSequence::toString
        ).subscribe {
            action(it)
        }.addToDisposable()
}