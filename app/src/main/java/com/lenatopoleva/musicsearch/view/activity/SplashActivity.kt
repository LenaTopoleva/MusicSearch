package com.lenatopoleva.musicsearch.view.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.utils.Event
import com.lenatopoleva.musicsearch.utils.ui.MainActivityAlertDialogFragment
import com.lenatopoleva.musicsearch.utils.ui.alertdialog.SplashActivityAlertDialogFragment
import com.lenatopoleva.musicsearch.viewmodel.activity.SplashViewModel
import org.koin.android.ext.android.getKoin

class SplashActivity: AppCompatActivity() {

    val model: SplashViewModel by lazy{
        ViewModelProvider(this, getKoin().get()).get(SplashViewModel::class.java)
    }

    private val authUserObserver = Observer<Event<Boolean>> { event ->
        event.getContentIfNotHandled()?.let { startMainActivity(it) } }
    private val errorObserver = Observer<Event<String>> { event -> event.getContentIfNotHandled()?.let {
        showAlertDialog(getString(R.string.error_stub), it) }}

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        model.authUserLiveDate.observe(this, authUserObserver)
        model.errorLiveData.observe(this, errorObserver)
        return super.onCreateView(name, context, attrs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            isUserAuth()
        }
    }

    private fun isUserAuth() = model.isUserAuth()

    private fun startMainActivity(isAuth: Boolean){
        MainActivity.start(this, isAuth)
        finish()
    }

    private fun showAlertDialog(title: String, message: String) {
        SplashActivityAlertDialogFragment.newInstance(title, message).show(supportFragmentManager,
            MainActivityAlertDialogFragment.DIALOG_TAG)
    }

}