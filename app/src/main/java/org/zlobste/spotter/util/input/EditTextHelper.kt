package org.zlobste.spotter.util.input

import com.google.android.material.textfield.TextInputLayout
import org.zlobste.spotter.R
import org.jetbrains.anko.textChangedListener
import org.tokend.muna.util.validator.EmailValidator
import org.tokend.muna.util.validator.PasswordValidator

object EditTextHelper {
    /**
     * Sets edit text validation based on [EmailValidator]
     */
    fun initEmailEditText(textLayout: TextInputLayout) {
        textLayout.editText?.textChangedListener {
            afterTextChanged { s ->
                if (s.isNullOrEmpty() || EmailValidator.isValid(s)) {
                    textLayout.error = null
                } else {
                    textLayout.error = textLayout.context.getString(R.string.error_invalid_email)
                }
            }
        }
    }

    /**
     * Sets edit text validation based on [PasswordValidator]
     */
    fun initPasswordEditText(textLayout: TextInputLayout) {
        textLayout.editText?.textChangedListener {
            afterTextChanged { s ->
                if (s.isNullOrEmpty() || PasswordValidator.isValid(s)) {
                    textLayout.error = null
                } else {
                    textLayout.error = textLayout.context.getString(R.string.error_weak_password)
                }
            }
        }
    }
}