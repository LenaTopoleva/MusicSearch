package com.lenatopoleva.musicsearch.utils.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

abstract class TextValidator(private val textView: TextView): TextWatcher {

    abstract fun validate(text: String)

    override fun afterTextChanged(p0: Editable?) {
        val text = textView.text.toString().trim()
        validate(text)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

}