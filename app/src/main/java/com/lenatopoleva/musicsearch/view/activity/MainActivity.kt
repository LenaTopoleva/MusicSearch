package com.lenatopoleva.musicsearch.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.databinding.ActivityMainBinding
import com.lenatopoleva.musicsearch.utils.ui.BackButtonListener
import com.lenatopoleva.musicsearch.viewmodel.activity.MainActivityViewModel
import org.koin.android.ext.android.getKoin
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : AppCompatActivity() {

    companion object{
        private const val IS_AUTH = "isUserAuth"
        fun start(context: Context, isUserAuth: Boolean) = Intent(context, MainActivity::class.java).apply {
            putExtra(IS_AUTH, isUserAuth)
            context.startActivity(this)
        }
    }

    private val navigatorHolder: NavigatorHolder by lazy { getKoin().get<NavigatorHolder>() }
    private val navigator = SupportAppNavigator(this, supportFragmentManager, R.id.container)

    val model: MainActivityViewModel by lazy {
        ViewModelProvider(this, getKoin().get()).get(MainActivityViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null) {
            val isAuth = intent.extras?.getBoolean(IS_AUTH)
            isAuth?.let { model.onCreate(it) }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backPressed()) {
                return
            }
        }
        model.backPressed()
    }
}