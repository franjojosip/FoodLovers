package ht.ferit.fjjukic.foodlovers.app_account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeLocationViewModel(
    private val userRepository: UserRepository,
    preferenceManager: PreferenceManager,
    analyticsProvider: AnalyticsProvider
) : BaseViewModel(analyticsProvider) {

    private val _user: MutableLiveData<UserModel> = MutableLiveData(preferenceManager.user)
    val user: LiveData<UserModel> = _user

    init {
        _user.value = preferenceManager.user
    }

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
                                withContext(Dispatchers.Main) {
                                    _user.value = it
                                }
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

}