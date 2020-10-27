package ht.ferit.fjjukic.foodlovers.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.AccountViewModel

@Suppress("UNCHECKED_CAST")
class AccountViewModelFactory(
    private val firebaseSource: FirebaseSource,
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountViewModel(firebaseSource, repository) as T
    }
}