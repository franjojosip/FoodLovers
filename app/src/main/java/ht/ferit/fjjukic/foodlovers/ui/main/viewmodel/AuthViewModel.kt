package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import ht.ferit.fjjukic.foodlovers.data.database.FirebaseDB
import ht.ferit.fjjukic.foodlovers.data.firebase.FirebaseSource
import ht.ferit.fjjukic.foodlovers.data.model.UserModel
import ht.ferit.fjjukic.foodlovers.data.repository.UserRepository
import ht.ferit.fjjukic.foodlovers.ui.common.AuthListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(
    private val firebaseSource: FirebaseSource,
    private val repository: UserRepository
) : ViewModel() {
    var email: String? = null
    var password: String? = null
    var repeatPassword: String? = null
    var username: String? = null
    var resetEmail: String? = null
    var authListener: AuthListener? = null

    private val disposables = CompositeDisposable()
    var firebaseUser = firebaseSource.currentUser()

    fun login() {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password")
            return
        }

        val disposable = firebaseSource.login(email!!, password!!)
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

        val disposable = firebaseSource.register(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                repository.insert(
                    UserModel(
                        "",
                        firebaseSource.currentUser()!!.uid,
                        username!!,
                        email!!,
                        "",
                        45.815399.toString(),
                        15.966568.toString()
                    )
                )
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

        val disposable = firebaseSource.resetPassword(resetEmail!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun deleteUser() {
        if (firebaseUser != null) {
            firebaseUser!!.delete()
            FirebaseDB().deleteUser(firebaseUser!!.uid)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}