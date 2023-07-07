package ht.ferit.fjjukic.foodlovers.app_account.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private val _user: MutableLiveData<UserModel> = MutableLiveData()
    val user: LiveData<UserModel> = _user


    fun init() {
        _user.value = preferenceManager.user
    }

    fun handleImagePathChange(value: Uri) {
        screenEvent.postValue(LoadingBar(true))
        _user.value?.let { user ->
            val ref = FirebaseStorage.getInstance().getReference("images/${user.userId}.jpg")
            ref.putFile(value).addOnSuccessListener {
                if (it.error != null) {
                    screenEvent.postValue(LoadingBar(false))
                    showMessage(
                        message = it.error?.message,
                        R.string.general_error_server
                    )
                }
                ref.downloadUrl.addOnSuccessListener { uri ->
                    user.imageUrl = uri.toString()
                    viewModelScope.launch {
                        handleResult(
                            {
                                userRepository.updateUser(user)
                            },
                            {
                                showMessage(messageId = R.string.image_change_success)
                            }, {
                                showMessage(messageId = R.string.image_change_error)
                            }
                        )
                    }
                }
            }
        }
    }

    fun handleNavigation(action: ActionNavigate) {
        actionNavigate.postValue(action)
    }

    fun onLogoutClick() {
        viewModelScope.launch(Dispatchers.IO) {
            screenEvent.postValue(LoadingBar(true))

            userRepository.logout()
//            actionNavigate.postValue(ActionNavigate.MainActivityNavigation)

            screenEvent.postValue(LoadingBar(false))
        }
    }
}
