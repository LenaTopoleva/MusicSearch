package com.lenatopoleva.musicsearch.view.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.utils.ui.MainActivityAlertDialogFragment
import com.lenatopoleva.musicsearch.viewmodel.activity.MainActivityViewModel
import com.lenatopoleva.musicsearch.viewmodel.base.BaseViewModel
import org.koin.android.ext.android.getKoin

abstract class BaseFragment : Fragment() {

    companion object {
        private const val DIALOG_FRAGMENT_TAG =
            "com.lenatopoleva.albumsearch.view.base.basefragment.dialogfragmenttag"
    }

    abstract val model: BaseViewModel
    protected var isNetworkAvailable: Boolean = false

    abstract fun hideLoader()

    abstract fun showLoader()

    abstract fun handleData(data: List<Media>)

    fun showAlertDialog(title: String, message: String) {
        MainActivityAlertDialogFragment.newInstance(title, message).show(childFragmentManager, DIALOG_FRAGMENT_TAG)
    }

}

