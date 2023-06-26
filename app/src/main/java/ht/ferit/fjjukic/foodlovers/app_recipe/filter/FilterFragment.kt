package ht.ferit.fjjukic.foodlovers.app_recipe.filter

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.view.BaseFragment
import ht.ferit.fjjukic.foodlovers.app_recipe.home.HomeViewModel
import ht.ferit.fjjukic.foodlovers.databinding.FragmentFilterBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterFragment : BaseFragment<HomeViewModel, FragmentFilterBinding>() {

    override val viewModel: HomeViewModel by sharedViewModel()
    override val layoutId: Int = R.layout.fragment_filter

    override fun init() {
        setScreen()
        setListeners()
    }

    private fun setScreen() {
        binding.cgCategories.setData(viewModel.getCategoryFilters())
        binding.cgTimes.setData(viewModel.getTimeFilters())
        binding.cgDifficulties.setData(viewModel.getDifficultyFilters())
        binding.cgSorts.setData(viewModel.getSortFilters())

        binding.cgCategories.selectChips(viewModel.selectedCategories)
        binding.cgTimes.selectChips(viewModel.selectedTimes)
        binding.cgDifficulties.selectChips(viewModel.selectedDifficulties)
        binding.cgSorts.selectChips(viewModel.selectedSorts)
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