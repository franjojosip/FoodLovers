package ht.ferit.fjjukic.foodlovers.app_common.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.widget.textChanges
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.listener.PermissionHandler
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.DialogModel
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.model.SnackbarModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.showAlertDialog
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseFragment<VM : BaseViewModel, ViewBinding : ViewDataBinding> : Fragment(),
    PermissionHandler {
    protected abstract val layoutId: Int
    protected abstract val viewModel: VM
    protected lateinit var binding: ViewBinding
    private var compositeDisposable = CompositeDisposable()

    protected var toolbar: CustomToolbarView? = null

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

        toolbar?.setupAction {
            findNavController().navigateUp()
        }
    }

    protected open fun setObservers() {
        viewModel.screenEvent.observe(viewLifecycleOwner) { screenEvent ->
            when (screenEvent) {
                is MessageModel -> showToast(screenEvent)
                is SnackbarModel -> showSnackbar(screenEvent)
                is DialogModel -> context?.showAlertDialog(screenEvent)
                is LoadingBar -> {
                    (binding.root.findViewById(R.id.loader_layout) as? View)?.isVisible =
                        screenEvent.isVisible
                }

                else -> {}
            }
        }
        viewModel.actionNavigate.observe(viewLifecycleOwner) {
            when (it) {
                is ActionNavigate.NavigationWithDirections -> {
                    findNavController().navigate(it.navDirections)
                }

                is ActionNavigate.Back -> findNavController().navigateUp()

                else -> handleActionNavigate(it)
            }
        }
    }

    protected open fun handleActionNavigate(actionNavigate: ActionNavigate) {
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

    protected fun showSnackbar(model: SnackbarModel) {
        val length = if (model.isShortLength) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG
        when {
            !model.message.isNullOrEmpty() -> {
                Snackbar.make(binding.root, model.message, length).show()
            }

            model.messageId != null -> {
                Snackbar.make(binding.root, getString(model.messageId), length).show()
            }
        }
    }

    private fun showToast(model: MessageModel) {
        val length = if (model.isShortLength) Toast.LENGTH_SHORT else Toast.LENGTH_LONG

        when {
            !model.message.isNullOrEmpty() -> {
                Toast.makeText(context, model.message, length).show()
            }

            model.messageId != null -> {
                Toast.makeText(context, getString(model.messageId), length).show()
            }
        }
    }

    protected fun requestRequiredPermissions(
        action: () -> Unit,
        permissions: Array<String>,
        messageId: Int,
        permissionLauncher: ActivityResultLauncher<Array<String>>
    ) {
        when {
            checkPermissions(
                activity as Context,
                permissions
            ) -> {
                action()
            }

            checkShouldShowPermissionRationale(requireActivity(), permissions) -> {
                showToast(getString(messageId))
                permissionLauncher.launch(permissions)
            }

            else -> {
                showToast(getString(R.string.general_error_permissions))
                openSettings(requireContext())
            }
        }
    }

    /**
     * RXJAVA
     */


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