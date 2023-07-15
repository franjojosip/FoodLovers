package ht.ferit.fjjukic.foodlovers.app_recipe.filter

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseAnalyticsConstants
import ht.ferit.fjjukic.foodlovers.app_recipe.search.SearchViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentFilterBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterFragment : BaseFragment<SearchViewModel, FragmentFilterBinding>() {

    override val screenConstant: String = FirebaseAnalyticsConstants.Event.Screen.FILTER

    override val viewModel: SearchViewModel by sharedViewModel()
    override val layoutId: Int = R.layout.fragment_filter

    override fun init() {
        setScreen()
        setListeners()
    }

    private fun setScreen() {
        binding.cgCategories.setData(viewModel.filter.categories)
        binding.cgTimes.setData(viewModel.filter.times)
        binding.cgDifficulties.setData(viewModel.filter.difficulties)
        binding.cgSorts.setData(viewModel.filter.sorts)
    }

    private fun setListeners() {
        binding.tvReset.setOnClickListener {
            binding.cgCategories.resetSelect()
            binding.cgTimes.resetSelect()
            binding.cgDifficulties.resetSelect()
            binding.cgSorts.resetSelect()
        }

        binding.tvConfirm.setOnClickListener {
            viewModel.onConfirmFilterClicked(
                binding.cgCategories.getSelectedChips(),
                binding.cgTimes.getSelectedChips(),
                binding.cgDifficulties.getSelectedChips(),
                binding.cgSorts.getSelectedChips()
            )
        }
    }
}