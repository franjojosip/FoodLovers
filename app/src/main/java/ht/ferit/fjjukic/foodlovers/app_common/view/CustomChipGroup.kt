package ht.ferit.fjjukic.foodlovers.app_common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem

class CustomChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ChipGroup(context, attrs, defStyleAttr) {

    private var defaultChipId: Int? = null
    private val chips: MutableList<FilterItem> = mutableListOf()

    fun getSelectedChips(): List<FilterItem> {
        val selectedChips = mutableListOf<FilterItem>()

        checkedChipIds.filter { it != defaultChipId }.forEach {
            findViewById<Chip>(it)?.let { chip ->
                chips.firstOrNull { item -> item.value == chip.text.toString() }?.let { selectedChip ->
                    selectedChips.add(selectedChip)
                }
            }
        }
        return selectedChips.toList()
    }

    fun resetSelect() {
        clearCheck()
        defaultChipId?.let { check(it) }
    }

    fun setData(items: List<FilterItem>) {
        chips.clear()
        items.forEach { item ->
            val chip = LayoutInflater.from(context).inflate(R.layout.chip_filter_item, null) as Chip
            chip.id = View.generateViewId()
            chip.text = item.value
            chip.isChecked = item.isChecked

            chips.add(item)

            if (item.isDefault) {
                defaultChipId = chip.id
            }

            chip.setOnCheckedChangeListener { view, isChecked ->
                onChipChanged(view.id, isChecked)
            }

            addView(chip)
        }
    }

    private fun onChipChanged(chipId: Int, isChecked: Boolean) {
        if (isChecked && chipId == defaultChipId) {
            children.filter { it.id != defaultChipId }.forEach {
                (it as? Chip)?.isChecked = false
            }
        }
        else if (isChecked && checkedChipIds.contains(defaultChipId)) {
            (children.firstOrNull { it.id == defaultChipId } as? Chip)?.isChecked = false
        }
        else if (!isChecked && checkedChipIds.isEmpty()) {
            defaultChipId?.let { check(it) }
        }
    }

    fun selectChips(chips: List<FilterItem>) {
        children.forEach { view ->
            val childChip = (view as? Chip)
            chips.firstOrNull { it.value == childChip?.text }?.let {
                childChip?.isChecked = true
            }
        }
    }

}