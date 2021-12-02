package com.lenatopoleva.musicsearch.view.activity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.utils.ui.MainActivityAlertDialogFragment
import com.lenatopoleva.musicsearch.utils.ui.alertdialog.SplashActivityAlertDialogFragment
import com.lenatopoleva.musicsearch.viewmodel.activity.SplashViewModel
import org.koin.android.ext.android.getKoin

class SplashActivity: AppCompatActivity() {

    val model: SplashViewModel by lazy{
        ViewModelProvider(this, getKoin().get()).get(SplashViewModel::class.java)
    }

    private val authStateObserver = Observer<AuthState> { renderData(it) }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        model.subscribe().observe(this, authStateObserver)
        return super.onCreateView(name, context, attrs)
    }

    override fun onResume() {
        super.onResume()
        isUserAuth()
    }

    private fun isUserAuth() = model.isUserAuth()

    private fun renderData(authState: AuthState){
        when(authState) {
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
        SplashActivityAlertDialogFragment. newInstance(title, message).show(supportFragmentManager, MainActivityAlertDialogFragment.DIALOG_TAG)
    }

}