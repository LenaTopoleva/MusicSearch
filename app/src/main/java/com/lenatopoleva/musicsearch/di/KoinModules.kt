package com.lenatopoleva.musicsearch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.model.datasource.db.RoomDatabase
import com.lenatopoleva.musicsearch.model.dispatchers.DispatcherProvider
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.interactor.ISplashInteractor
import com.lenatopoleva.musicsearch.model.interactor.SplashInteractor
import com.lenatopoleva.musicsearch.model.repository.AuthRepository
import com.lenatopoleva.musicsearch.model.repository.IAuthRepository
import com.lenatopoleva.musicsearch.viewmodel.activity.MainActivityViewModel
import com.lenatopoleva.musicsearch.viewmodel.activity.SplashViewModel
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Provider

val application = module {
    single<IAuthRepository> { AuthRepository(RoomDatabase()) }
    single<IDispatcherProvider> { DispatcherProvider() }
}

val viewModelModule = module {
    single<MutableMap<Class<out ViewModel>, Provider<ViewModel>>> {
        val map =
            mutableMapOf(
                MainActivityViewModel::class.java to Provider<ViewModel>{
                    MainActivityViewModel ( get<Router>()) },
                SplashViewModel::class.java to Provider<ViewModel>{ SplashViewModel(get(), get(), get()) }
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
    factory { SplashViewModel(get(), get(), get()) }
}

val mainActivity = module {
    factory { MainActivityViewModel(get<Router>()) }
}


