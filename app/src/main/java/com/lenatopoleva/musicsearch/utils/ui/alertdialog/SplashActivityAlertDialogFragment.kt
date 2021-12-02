package com.lenatopoleva.musicsearch.utils.ui.alertdialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.utils.ui.MainActivityAlertDialogFragment
import com.lenatopoleva.musicsearch.viewmodel.activity.SplashViewModel
import org.koin.android.ext.android.getKoin


class SplashActivityAlertDialogFragment : MainActivityAlertDialogFragment() {

    override val model by lazy {
        ViewModelProvider(requireActivity(), getKoin().get())[SplashViewModel::class.java]
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (model as? AlertDialogListener)?.alertDialogBtnOkClicked()
    }

    companion object{
        fun newInstance(title: String, message: String): SplashActivityAlertDialogFragment {
            val args = Bundle()
            args.putString(TITLE_EXTRA, title)
            args.putString(MESSAGE_EXTRA, message)
            val splashDialogFragment = SplashActivityAlertDialogFragment()
            splashDialogFragment.arguments = args
            return splashDialogFragment
        }
    }

}