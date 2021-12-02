package com.lenatopoleva.musicsearch.utils.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.utils.ui.alertdialog.AlertDialogListener
import com.lenatopoleva.musicsearch.viewmodel.activity.MainActivityViewModel
import org.koin.android.ext.android.getKoin

open class MainActivityAlertDialogFragment: AppCompatDialogFragment() {

    open val onOkClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, _ ->
        (model as? AlertDialogListener)?.alertDialogBtnOkClicked()
        dialog?.dismiss()
    }

    open val model: ViewModel by lazy {
        ViewModelProvider(requireActivity(),  getKoin().get())[MainActivityViewModel::class.java]
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

        fun newInstance(title: String, message: String): MainActivityAlertDialogFragment {
            val args = Bundle()
            args.putString(TITLE_EXTRA, title)
            args.putString(MESSAGE_EXTRA, message)
            val splashDialogFragment = MainActivityAlertDialogFragment()
            splashDialogFragment.arguments = args
            return splashDialogFragment
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
        builder.setPositiveButton(context.getString(R.string.ok), onOkClickListener)
        return builder.create()
    }
}