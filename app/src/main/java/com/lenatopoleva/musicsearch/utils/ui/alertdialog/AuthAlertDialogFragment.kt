package com.lenatopoleva.musicsearch.utils.ui.alertdialog

import android.os.Bundle
import com.lenatopoleva.musicsearch.utils.ui.AlertDialogFragment

class AuthAlertDialogFragment: AlertDialogFragment() {

    companion object {
        fun newInstance(title: String, message: String): AuthAlertDialogFragment {
            val args = Bundle()
            args.putString(TITLE_EXTRA, title)
            args.putString(MESSAGE_EXTRA, message)
            val splashDialogFragment = AuthAlertDialogFragment()
            splashDialogFragment.arguments = args
            return splashDialogFragment
        }
    }
}