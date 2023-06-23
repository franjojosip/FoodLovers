package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Step
import ht.ferit.fjjukic.foodlovers.databinding.StepListItemBinding

class StepItemViewHolder(private val binding: StepListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(step: Step) {
        binding.tvStep.text = "Step ${step.position}."
        binding.tvDescription.text = step.description
    }
}