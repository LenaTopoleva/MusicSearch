package com.lenatopoleva.musicsearch.utils.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.viewmodel.activity.SplashViewModel
import org.koin.android.ext.android.getKoin


open class AuthAlertDialogFragment : AppCompatDialogFragment() {

    private val model by lazy {
        ViewModelProvider(requireActivity(), getKoin().get())[SplashViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alertDialog = getStubAlertDialog(requireContext())
        val args = arguments
        if (args != null) {
            val title = args.getString(TITLE_EXTRA)
            val message = args.getString(MESSAGE_EXTRA)
            alertDialog = getAlertDialog(requireContext(), title, message)
        }
        return alertDialog
    }

    companion object {
        const val DIALOG_TAG = "com.lenatopoleva.musicsearch.utils.ui.alertdialogfragment"
        private const val TITLE_EXTRA = "com.lenatopoleva.musicsearch.utils.ui.titleextra"
        private const val MESSAGE_EXTRA = "com.lenatopoleva.musicsearch.utils.ui.messageextra"

        fun newInstance(title: String?, message: String?): AuthAlertDialogFragment {
            val dialogFragment = AuthAlertDialogFragment()
            val args = Bundle()
            args.putString(TITLE_EXTRA, title)
            args.putString(MESSAGE_EXTRA, message)
            dialogFragment.arguments = args
            return dialogFragment
        }
    }

    private fun getStubAlertDialog(context: Context): AlertDialog {
        return getAlertDialog(context, null, null)
    }

    private fun getAlertDialog(context: Context, title: String?, message: String?): AlertDialog {
        val builder = AlertDialog.Builder(context)
        var finalTitle: String? = context.getString(R.string.dialog_title_stub)
        if (!title.isNullOrBlank()) {
            finalTitle = title
        }
        builder.setTitle(finalTitle)
        if (!message.isNullOrBlank()) {
            builder.setMessage(message)
        }
        builder.setCancelable(true)
        builder.setPositiveButton("OK") { dialog, _ ->
            run {
                model.alertBtnOkClicked()
                dialog.dismiss()
            }
        }
        return builder.create()
    }
}