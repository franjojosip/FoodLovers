package ht.ferit.fjjukic.foodlovers.app_common.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.internal.ViewUtils.requestFocusAndShowKeyboard
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.*
import ht.ferit.fjjukic.foodlovers.databinding.LayoutSearchBinding

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutSearchBinding =
        LayoutSearchBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setUpView()
        setUpListener()
    }

    private fun setUpView() {
        binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_search,
            0,
            0,
            0
        )
        binding.etSearch.onDrawableEndClick {
            binding.etSearch.setText("")
        }
        binding.etSearch.hideKeyboardOnLostFocus()
    }

    private fun setUpListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                when {
                    (p0?.length ?: 0) > 0 -> {
                        binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_search,
                            0,
                            R.drawable.ic_close,
                            0
                        )
                    }
                    else -> {
                        binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_search,
                            0,
                            0,
                            0
                        )

                    }
                }
            }

        })
    }

    fun handleViewClicked(action: () -> Unit) {
        binding.etSearch.setOnClickListener {
            action()
        }
    }

    fun handleEndIconClicked(action: () -> Unit) {
        binding.etSearch.onDrawableEndClick {
            binding.etSearch.setText("")
            showKeyboardAndFocus()
            action()
        }
    }

    fun handleSearch(action: (String) -> Unit) {
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                textView.clearFocusAndHideKeyboard()
                if (!textView.text.isNullOrEmpty()) {
                    action(textView.text.toString())
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun EditText.onDrawableEndClick(action: () -> Unit) {
        setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                view as EditText
                val end =
                    if (view.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL)
                        view.left else view.right
                if (event.rawX >= (end - view.compoundPaddingEnd)) {
                    action.invoke()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    fun disableSearch() {
        binding.etSearch.isClickable = true
        binding.etSearch.isCursorVisible = false
        binding.etSearch.isFocusable = false
    }

    @SuppressLint("RestrictedApi")
    fun showKeyboardAndFocus() {
        requestFocusAndShowKeyboard(binding.etSearch)
    }
}