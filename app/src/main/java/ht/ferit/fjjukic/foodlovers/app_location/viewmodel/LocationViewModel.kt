package ht.ferit.fjjukic.foodlovers.app_location.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.live_data.SingleLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class LocationViewModel(
    private val repository: UserRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private val _refreshUser: SingleLiveData<Boolean> = SingleLiveData()
    private val _user: MutableLiveData<UserModel> = MutableLiveData(preferenceManager.user)

    val refreshUser: LiveData<Boolean> = _refreshUser
    val currentUser: LiveData<UserModel> = _user

    fun updateUserLocation(currentLatLng: LatLng) {
        val updatedUser = currentUser.value
        updatedUser?.let {
            it.latitude = currentLatLng.latitude.toString()
            it.longitude = currentLatLng.longitude.toString()

            repository.update(it)
                .subscribeIO()
                .observeMain()
                .subscribeWithResult({ isSuccess ->
                    if (isSuccess) {
                        _user.postValue(preferenceManager.user)
                        _refreshUser.postValue(true)
                        showMessage(messageId = R.string.location_change_success)
                    } else {
                        showMessage(messageId = R.string.location_change_error)
                    }
                }, ::handleError)
        }
    }

    fun handleNavigateAction(action: ActionNavigate) {
        _actionNavigate.postValue(action)
    }
}