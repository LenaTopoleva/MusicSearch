package com.lenatopoleva.musicsearch.utils.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.lenatopoleva.musicsearch.R

open class AlertDialogFragment: AppCompatDialogFragment() {

    open val onOkClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, _ ->
        dialog?.dismiss()
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


    companion object Creator {
        const val DIALOG_TAG = "com.lenatopoleva.musicsearch.utils.ui.basealertdialogfragment"
        const val TITLE_EXTRA = "com.lenatopoleva.musicsearch.utils.ui.titleextra"
        const val MESSAGE_EXTRA = "com.lenatopoleva.musicsearch.utils.ui.messageextra"
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
        builder.setPositiveButton(getString(R.string.ok), onOkClickListener)
        return builder.create()
    }
}