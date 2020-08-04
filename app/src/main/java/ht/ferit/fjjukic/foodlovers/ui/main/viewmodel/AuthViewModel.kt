package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import ht.ferit.fjjukic.foodlovers.data.model.User
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import ht.ferit.fjjukic.foodlovers.utils.startLoginActivity
import ht.ferit.fjjukic.foodlovers.utils.startRegisterActivity
import ht.ferit.fjjukic.foodlovers.utils.startResetPasswordActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {
    var email: String? = null
    var password: String? = null
    var repeatPassword: String? = null
    var username: String? = null
    var resetEmail: String? = null
    var authListener: AuthListener? = null

    private val disposables = CompositeDisposable()
    var firebaseUser = repository.currentUser()

    fun login() {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password")
            return
        }

        val disposable = repository.login(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun register() {
        if (email.isNullOrEmpty() || password.isNullOrEmpty() || repeatPassword.isNullOrEmpty() || username.isNullOrEmpty()) {
            authListener?.onFailure("Please input all values")
            return
        }

        if (!repeatPassword.equals(password)) {
            authListener?.onFailure("Passwords don't match")
            return
        }

        val disposable = repository.register(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                repository.insert(User(email!!, username!!, "", 45.815399, 15.966568))
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun resetPassword() {
        if (resetEmail.isNullOrEmpty()) {
            authListener?.onFailure("Fail to send reset password email, please fill email field!")
            return
        }

        val disposable = repository.resetPassword(resetEmail!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun deleteUser(){
        if(firebaseUser != null){
            firebaseUser!!.delete()
        }
    }

    fun goToRegister(view: View) {
        view.context.startRegisterActivity()
    }

    fun goToLogin(view: View) {
        view.context.startLoginActivity()
    }

    fun goToResetPassword(view: View) {
        view.context.startResetPasswordActivity()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}