package com.lenatopoleva.musicsearch.view.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.utils.ui.AlertDialogFragment
import com.lenatopoleva.musicsearch.utils.ui.alertdialog.SplashAlertDialogFragment
import com.lenatopoleva.musicsearch.viewmodel.activity.SplashViewModel
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.getKoin

class SplashActivity: AppCompatActivity() {

    val model: SplashViewModel by lazy{
        ViewModelProvider(this, getKoin().get()).get(SplashViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            model.subscribe().collect { renderData(it) }
        }
        isUserAuth()
    }

    private fun isUserAuth() = model.isUserAuth()

    private fun renderData(authState: AuthState){
        when(authState) {
            is AuthState.Initial -> {}
            is AuthState.Success -> {
                authState.data?.let { startMainActivity(true) }
                    ?: startMainActivity(false)
            }
            is AuthState.Error ->  {
                showAlertDialog(getString(R.string.error_stub),
                    authState.error.message ?: "")
            }
        }
    }

    private fun startMainActivity(isAuth: Boolean){
        MainActivity.start(this, isAuth)
        finish()
    }

    private fun showAlertDialog(title: String, message: String) {
        SplashAlertDialogFragment. newInstance(title, message).show(supportFragmentManager, AlertDialogFragment.DIALOG_TAG)
    }

}