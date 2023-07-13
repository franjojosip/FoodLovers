package ht.ferit.fjjukic.foodlovers.app_common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ht.ferit.fjjukic.foodlovers.app_common.listener.PermissionHandler
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.DialogModel
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.model.ScreenEvent
import ht.ferit.fjjukic.foodlovers.app_common.model.SnackbarModel
import ht.ferit.fjjukic.foodlovers.app_common.utils.showAlertDialog
import ht.ferit.fjjukic.foodlovers.app_common.view.CustomToolbarView
import ht.ferit.fjjukic.foodlovers.app_main.main.MainActivity
import ht.ferit.fjjukic.foodlovers.app_main.prelogin.PreloginActivity

@Suppress("EmptyMethod")
abstract class BaseFragment<VM : BaseViewModel, ViewBinding : ViewDataBinding> : Fragment(),
    PermissionHandler {
    protected abstract val layoutId: Int
    protected abstract val viewModel: VM
    protected lateinit var binding: ViewBinding

    protected var toolbar: CustomToolbarView? = null
    protected var loader: View? = null

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
                is DialogModel -> context?.showAlertDialog(screenEvent)
                is LoadingBar -> {
                    loader?.visibility = if (screenEvent.isVisible) View.VISIBLE else View.GONE
                }

                else -> handleScreenEvent(screenEvent)
            }
        }
        viewModel.messageScreenEvent.observe(viewLifecycleOwner) { screenEvent ->
            when (screenEvent) {
                is MessageModel -> showToast(screenEvent)
                is SnackbarModel -> showSnackbar(screenEvent)
                else -> {}
            }
        }
        viewModel.actionNavigate.observe(viewLifecycleOwner) {
            when (it) {
                is ActionNavigate.NavigationWithDirections -> {
                    findNavController().navigate(it.navDirections)
                }

                is ActionNavigate.Back -> findNavController().navigateUp()

                is ActionNavigate.MainScreen -> {
                    (activity as? PreloginActivity)?.navigateToMainActivity()
                }

                is ActionNavigate.Prelogin -> {
                    (activity as? MainActivity)?.navigateToPrelogin()
                }

                else -> handleActionNavigate(it)
            }
        }
    }

    protected open fun handleActionNavigate(actionNavigate: ActionNavigate) {}
    protected open fun handleScreenEvent(screenEvent: ScreenEvent) {}

    abstract fun init()

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
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

    private fun showSnackbar(model: SnackbarModel) {
        val length = if (model.isShortLength) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG
        when {
            !model.message.isNullOrBlank() -> {
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
            checkPermissions(requireContext(), permissions) -> {
                action()
            }

            checkShouldShowPermissionRationale(requireActivity(), permissions) -> {
                showToast(getString(messageId))
                permissionLauncher.launch(permissions)
            }

            else -> {
                showToast(getString(messageId))
                openSettings(requireContext())
            }
        }
    }
}