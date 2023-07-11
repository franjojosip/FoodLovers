package ht.ferit.fjjukic.foodlovers.app_common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VM : BaseViewModel, ViewBinding : ViewDataBinding> :
    AppCompatActivity() {
    abstract val layoutId: Int
    abstract val viewModel: VM?
    lateinit var binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        setContentView(binding.root)
        init()
    }

    abstract fun init()

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}