package com.lenatopoleva.musicsearch.di

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.model.datasource.db.RoomDatabase
import com.lenatopoleva.musicsearch.model.datasource.network.Retrofit
import com.lenatopoleva.musicsearch.model.dispatchers.DispatcherProvider
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.imageloader.IImageLoader
import com.lenatopoleva.musicsearch.model.interactor.*
import com.lenatopoleva.musicsearch.model.repository.*
import com.lenatopoleva.musicsearch.view.imageloader.GlideImageLoader
import com.lenatopoleva.musicsearch.viewmodel.activity.MainActivityViewModel
import com.lenatopoleva.musicsearch.viewmodel.activity.SplashViewModel
import com.lenatopoleva.musicsearch.viewmodel.fragment.AlbumsViewModel
import com.lenatopoleva.musicsearch.viewmodel.fragment.AuthViewModel
import com.lenatopoleva.musicsearch.viewmodel.fragment.RegistrationViewModel
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Provider

val application = module {
    single<IRepository> { Repository(Retrofit()) }
    single<IRepositoryLocal> { RepositoryLocal(RoomDatabase()) }
    single<IAuthRepository> { AuthRepository(RoomDatabase()) }
    single<IDispatcherProvider> { DispatcherProvider() }
    single<IImageLoader<ImageView>> { GlideImageLoader() }

}

val viewModelModule = module {
    single<MutableMap<Class<out ViewModel>, Provider<ViewModel>>> {
        val map =
            mutableMapOf(
                MainActivityViewModel::class.java to Provider<ViewModel>{
                    MainActivityViewModel ( get<Router>()) },
                SplashViewModel::class.java to Provider<ViewModel>{ SplashViewModel(get(), get()) },
                AuthViewModel::class.java to Provider<ViewModel> { AuthViewModel(get(), get(), get()) },
                RegistrationViewModel::class.java to Provider<ViewModel> { RegistrationViewModel(get(), get(), get()) },
                AlbumsViewModel::class.java to Provider<ViewModel>{
                    AlbumsViewModel (get<IAlbumsInteractor>(), get<Router>(), get<IDispatcherProvider>()) }
            )
        map
    }
    single<ViewModelProvider.Factory> { ViewModelFactory(get())}
}

val navigation = module {
    val cicerone: Cicerone<Router> = Cicerone.create()
    factory<NavigatorHolder> { cicerone.navigatorHolder }
    factory<Router> { cicerone.router }
}

val splashActivity = module {
    factory<ISplashInteractor> { SplashInteractor(get()) }
    factory { SplashViewModel(get(), get()) }
}

val mainActivity = module {
    factory { MainActivityViewModel(get<Router>()) }
}

val albumDetailsFragment = module {

}

val authFragment = module {
    factory { AuthViewModel(get(), get(), get()) }
    factory<IAuthInteractor> { AuthInteractor(get()) }
}

val registrationFragment = module {
    factory { RegistrationViewModel(get(), get(), get()) }
}

val albumsFragment = module {
    factory { AlbumsViewModel(get(), get(), get()) }
    factory<IAlbumsInteractor> { AlbumsInteractor(get(), get(), get()) }
}


