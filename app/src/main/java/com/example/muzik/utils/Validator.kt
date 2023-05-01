package com.example.muzik.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.util.Patterns
import android.widget.EditText


class Validator {

    companion object {
        fun validateEmail(email: String): String? {
            return if (TextUtils.isEmpty(email)) {
                "Email không được để trống"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                "Email không hợp lệ"
            } else {
                null
            }
        }

        fun validatePassword(password: String): String? {
            return if (TextUtils.isEmpty(password)) {
                "Mật khẩu không được để trống"
            } else if (password.length < 6) {
                "Mật khẩu phải chứa ít nhất 6 ký tự"
            } else {
                null
            }
        }

        fun validateIsEmpty(editText: EditText, errorMessage: String): String? {
            return if (TextUtils.isEmpty(editText.text.toString().trim())) {
                errorMessage
            } else {
                null
            }
        }

        fun validateName(name: String): String? {
            return if (!name.matches(Regex("^[a-zA-Z0-9 ]+$"))) {
                "Tên không được chứa ký tự đặc biệt"
            } else {
                null
            }
        }
    }
}
