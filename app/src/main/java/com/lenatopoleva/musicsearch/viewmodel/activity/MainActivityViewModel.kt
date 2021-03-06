package com.lenatopoleva.musicsearch.viewmodel.activity

import androidx.lifecycle.ViewModel
import com.lenatopoleva.musicsearch.navigation.Screens
import com.lenatopoleva.musicsearch.utils.ui.alertdialog.AlertDialogListener
import ru.terrakok.cicerone.Router

class MainActivityViewModel(private val router: Router): ViewModel(), AlertDialogListener {

    override fun alertDialogBtnOkClicked(){}

    fun backPressed() {
        router.exit()
    }

    fun onCreate(isAuth: Boolean) {
        if(isAuth) router.replaceScreen(Screens.AlbumsScreen())
        else router.replaceScreen(Screens.AuthScreen())
    }

}