package ht.ferit.fjjukic.foodlovers.app_account.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.app_common.live_data.SingleLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.MessageModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.user.UserRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class AccountViewModel(
    private val firebaseSource: FirebaseSource,
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private val _refreshUser: SingleLiveData<Boolean> = SingleLiveData()
    val refreshUser: LiveData<Boolean> = _refreshUser

    private val _user: MutableLiveData<UserModel> = MutableLiveData()
    val currentUser: LiveData<UserModel> = _user

    val actionNavigate: LiveData<ActionNavigate> = _actionNavigate
    val showMessage: LiveData<MessageModel> = _showMessage
    val showLoading: LiveData<Boolean> = _showLoading

    fun init() {
        _user.postValue(preferenceManager.user)
    }

    fun handleImagePathChange(value: Uri) {
        _showLoading.postValue(true)
        currentUser.value?.let { user ->
            val ref = FirebaseStorage.getInstance().getReference("images/${user.userId}.jpg")
            ref.putFile(value).addOnSuccessListener {
                if (it.error != null) {
                    _showLoading.postValue(false)
                    showMessage(
                        message = it.error?.message,
                        R.string.general_error_server
                    )
                }
                ref.downloadUrl.addOnSuccessListener { uri ->
                    user.imageUrl = uri.toString()
                    userRepository.update(user)
                        .subscribeIO()
                        .observeMain()
                        .subscribeWithResult({ isSuccess ->
                            if (isSuccess) {
                                showMessage(messageId = R.string.image_change_success)
                                _refreshUser.postValue(true)
                            } else {
                                showMessage(messageId = R.string.image_change_error)
                            }
                        }, ::handleError)
                }
            }
        }
    }

    fun handleNavigateAction(action: ActionNavigate) {
        if (action is ActionNavigate.Logout) {
            firebaseSource.logout()
            _actionNavigate.postValue(ActionNavigate.Login)
        } else {
            _actionNavigate.postValue(action)
        }
    }
}
