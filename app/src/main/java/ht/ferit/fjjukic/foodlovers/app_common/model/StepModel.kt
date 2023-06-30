package ht.ferit.fjjukic.foodlovers.app_common.model

data class StepModel(
    var position: Int,
    var description: String
) {
    constructor() : this(1, "")
}