package com.lenatopoleva.musicsearch.navigation

import com.lenatopoleva.musicsearch.view.fragment.AlbumDetailsFragment
import com.lenatopoleva.musicsearch.view.fragment.AlbumsFragment
import com.lenatopoleva.musicsearch.view.fragment.AuthFragment
import com.lenatopoleva.musicsearch.view.fragment.RegistrationFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class AlbumsScreen(): SupportAppScreen() {
        override fun getFragment() = AlbumsFragment.newInstance()
    }

    class AuthScreen(): SupportAppScreen() {
        override fun getFragment() = AuthFragment.newInstance()
    }

    class RegistrationScreen(): SupportAppScreen(){
        override fun getFragment() = RegistrationFragment.newInstance()
    }

    class AlbumDetailsScreen(val albumId: Int): SupportAppScreen() {
        override fun getFragment() = AlbumDetailsFragment.newInstance(albumId)
    }

}