package com.lenatopoleva.musicsearch.navigation

import com.lenatopoleva.musicsearch.view.fragment.AlbumsFragment
import com.lenatopoleva.musicsearch.view.activity.SplashActivity
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
//    class SplashScreen(): SupportAppScreen(){
//        override fun getFragment() = SplashActivity.newInstance()
//    }

    class AlbumsScreen(): SupportAppScreen() {
        override fun getFragment() = AlbumsFragment.newInstance()
    }
}