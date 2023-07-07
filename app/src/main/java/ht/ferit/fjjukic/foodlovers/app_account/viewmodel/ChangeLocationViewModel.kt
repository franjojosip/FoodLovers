package ht.ferit.fjjukic.foodlovers.app_account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeLocationViewModel(
    private val userRepository: UserRepository,
    preferenceManager: PreferenceManager
) : BaseViewModel() {

    private val _user: MutableLiveData<UserModel> = MutableLiveData(preferenceManager.user)
    val user: LiveData<UserModel> = _user

    fun updateUserLocation(currentLatLng: LatLng) {
        val updatedUser = user.value
        updatedUser?.let {
            updatedUser.latitude = currentLatLng.latitude.toString()
            updatedUser.longitude = currentLatLng.longitude.toString()

            viewModelScope.launch(Dispatchers.IO) {
                handleResult({
                    userRepository.updateUser(updatedUser)
                }, { result ->
                    when (result) {
                        true -> {
                            updatedUser.let {
                                _user.postValue(it)
                            }
                            showMessage(messageId = R.string.location_change_success)
                        }

                        else -> showMessage(messageId = R.string.location_change_error)
                    }
                }, {
                    showMessage(messageId = R.string.location_change_error)
                })
            }
        }
    }

    fun handleNavigateAction(action: ActionNavigate) {
        actionNavigate.postValue(action)
    }
}