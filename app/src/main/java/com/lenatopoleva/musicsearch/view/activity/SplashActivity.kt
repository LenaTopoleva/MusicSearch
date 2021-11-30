package com.lenatopoleva.musicsearch.view.activity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.utils.ui.AuthAlertDialogFragment
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
                    authState.error.message)
            }
        }
    }

    private fun startMainActivity(isAuth: Boolean){
        MainActivity.start(this,true)
        finish()
    }

    private fun showAlertDialog(title: String?, message: String?) {
        println("showAlertDialog")
        AuthAlertDialogFragment.newInstance(title, message).show(supportFragmentManager, AuthAlertDialogFragment.DIALOG_TAG)
    }

}