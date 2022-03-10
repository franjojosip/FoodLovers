package ht.ferit.fjjukic.foodlovers.app_common.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionEvent
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class BaseActivity<VM : BaseViewModel, ViewBinding : ViewDataBinding> :
    AppCompatActivity() {
    abstract val layoutId: Int
    abstract val viewModel: VM
    lateinit var binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        setContentView(binding.root)
        init()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    open fun handleEvent(event: ActionEvent) {}

    abstract fun init()

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}