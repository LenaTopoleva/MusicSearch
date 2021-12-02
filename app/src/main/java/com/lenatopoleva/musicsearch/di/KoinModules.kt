package com.lenatopoleva.musicsearch.di

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lenatopoleva.musicsearch.model.datasource.db.Database
import com.lenatopoleva.musicsearch.model.datasource.db.RoomDatabase
import com.lenatopoleva.musicsearch.model.datasource.db.dao.AlbumsDao
import com.lenatopoleva.musicsearch.model.datasource.db.dao.TracksDao
import com.lenatopoleva.musicsearch.model.datasource.db.dao.UsersDao
import com.lenatopoleva.musicsearch.model.datasource.network.BaseInterceptor
import com.lenatopoleva.musicsearch.model.datasource.network.RetrofitNetwork
import com.lenatopoleva.musicsearch.model.dispatchers.DispatcherProvider
import com.lenatopoleva.musicsearch.model.dispatchers.IDispatcherProvider
import com.lenatopoleva.musicsearch.model.imageloader.IImageLoader
import com.lenatopoleva.musicsearch.model.interactor.activity.ISplashInteractor
import com.lenatopoleva.musicsearch.model.interactor.activity.SplashInteractor
import com.lenatopoleva.musicsearch.model.interactor.fragment.*
import com.lenatopoleva.musicsearch.model.repository.*
import com.lenatopoleva.musicsearch.view.imageloader.GlideImageLoader
import com.lenatopoleva.musicsearch.viewmodel.activity.MainActivityViewModel
import com.lenatopoleva.musicsearch.viewmodel.activity.SplashViewModel
import com.lenatopoleva.musicsearch.viewmodel.fragment.AlbumDetailsViewModel
import com.lenatopoleva.musicsearch.viewmodel.fragment.AlbumsViewModel
import com.lenatopoleva.musicsearch.viewmodel.fragment.AuthViewModel
import com.lenatopoleva.musicsearch.viewmodel.fragment.RegistrationViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Provider

val application = module {

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(RetrofitNetwork.BASE_URL_LOCATIONS)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(get())
            .build()}

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(BaseInterceptor.interceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    single<Gson> {
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create()
    }

    single { Room.databaseBuilder(get(), Database::class.java, "MusicSearchDB").build() }
    single<AlbumsDao> { get<Database>().albumsDao() }
    single<TracksDao> { get<Database>().tracksDao() }
    single<UsersDao> { get<Database>().usersDao() }

    single<IRepository> { Repository(RetrofitNetwork(get())) }
    single<IRepositoryLocal> { RepositoryLocal(RoomDatabase(get(), get(), get(), get())) }
    single<IAuthRepository> { AuthRepository(RoomDatabase(get(), get(), get(), get())) }

    //Password hashing
    single<Argon2Kt> { Argon2Kt()}

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
                    AlbumsViewModel (get<IAlbumsInteractor>(), get<Router>(), get<IDispatcherProvider>()) },
                AlbumDetailsViewModel::class.java to Provider<ViewModel>{
                    AlbumDetailsViewModel(get<IAlbumDetailsInteractor>(), get<Router>(), get<IDispatcherProvider>()) }
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

val albumDetailsFragment = module {
    factory { AlbumDetailsViewModel(get(), get(), get()) }
    factory<IAlbumDetailsInteractor> { AlbumDetailsInteractor(get(), get()) }
}


