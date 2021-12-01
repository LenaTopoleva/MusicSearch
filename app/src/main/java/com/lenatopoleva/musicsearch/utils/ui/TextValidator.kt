package com.lenatopoleva.musicsearch.utils.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

abstract class TextValidator(private val textView: TextView): TextWatcher {

    companion object{
        const val NAME = "name"
        const val SURNAME = "surname"
        const val AGE = "age"
        const val PHONE = "phone"
        const val EMAIL = "email"
        const val PASSWORD = "password"

        const val NAME_PATTERN = "[a-zA-Z]+"
        const val SURNAME_PATTERN = NAME_PATTERN
        const val AGE_PATTERN = "^(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](19|20)\\d\\d\$" //dd.MM.yyyy
        const val PHONE_PATTERN = "^[+]7[ ][(][0-9]{3}[)][ ][0-9]{3}[-][0-9]{2}[-][0-9]{2}$" //+7 (***) ***-**-**
        const val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        const val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z[0-9]]{6,}$"
    }

    abstract fun validate(text: String)

    override fun afterTextChanged(p0: Editable?) {
        val text = textView.text.toString().trim()
        validate(text)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

}