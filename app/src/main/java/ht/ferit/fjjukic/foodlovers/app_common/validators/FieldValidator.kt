package ht.ferit.fjjukic.foodlovers.app_common.validators

import android.util.Patterns.EMAIL_ADDRESS
import ht.ferit.fjjukic.foodlovers.R

object FieldValidator {
    private const val EMAIL_MAX_LENGTH = 40

    private const val PASSWORD_MAX_LENGTH = 16
    private const val PASSWORD_MIN_LENGTH = 6

    private const val USERNAME_MAX_LENGTH = 16
    private const val USERNAME_MIN_LENGTH = 4

    fun checkEmail(value: String): Int? {
        return if (EMAIL_ADDRESS.matcher(value).matches() && value.length <= EMAIL_MAX_LENGTH) null
        else R.string.email_field_error
    }

    fun checkPassword(value: String): Int? {
        return if (value.isNotEmpty() && value.length >= PASSWORD_MIN_LENGTH && value.length <= PASSWORD_MAX_LENGTH) null
        else R.string.password_field_error
    }

    fun checkRepeatedPassword(value1: String, value2: String): Int? {
        return if (value1 == value2 && value1.isNotEmpty() && value2.isNotEmpty()) null
        else R.string.password_not_match_field_error
    }

    fun checkUsername(value: String): Int? {
        return if (value.isNotEmpty() && value.length >= USERNAME_MIN_LENGTH && value.length <= USERNAME_MAX_LENGTH) null
        else R.string.username_field_error
    }

    fun checkSameUsername(value1: String, value2: String): Int? {
        return if (value1 != value2 && value1.isNotEmpty() && value2.isNotEmpty()) null
        else R.string.username_same_as_old_error
    }

    fun checkSameEmail(value1: String, value2: String): Int? {
        return if (value1 != value2 && value1.isNotEmpty() && value2.isNotEmpty()) null
        else R.string.email_same_as_old_field_error
    }

}