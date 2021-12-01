package com.lenatopoleva.musicsearch.utils.ui
import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy

class InputHelper {

    companion object{
        fun getInstance(editText: EditText): MaskedTextChangedListener = MaskedTextChangedListener.Companion.installOn(
            editText,
            "+7 ([000]) [000]-[00]-[00]",
            affineFormats = emptyList(),
            AffinityCalculationStrategy.WHOLE_STRING,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {}
            }
        )
    }

}