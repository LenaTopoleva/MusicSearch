package com.lenatopoleva.musicsearch.utils.ui.alertdialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.utils.ui.AlertDialogFragment
import com.lenatopoleva.musicsearch.viewmodel.activity.SplashViewModel
import org.koin.android.ext.android.getKoin


class SplashAlertDialogFragment : AlertDialogFragment() {

    private val model by lazy {
        ViewModelProvider(requireActivity(), getKoin().get())[SplashViewModel::class.java]
    }

    override val onOkClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, _ ->
        model.alertBtnOkClicked()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        model.alertBtnOkClicked()
    }

    companion object{
        fun newInstance(title: String, message: String): SplashAlertDialogFragment {
            val args = Bundle()
            args.putString(TITLE_EXTRA, title)
            args.putString(MESSAGE_EXTRA, message)
            val splashDialogFragment = SplashAlertDialogFragment()
            splashDialogFragment.arguments = args
            return splashDialogFragment
        }
    }

}