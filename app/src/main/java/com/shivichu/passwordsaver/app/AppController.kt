package com.shivichu.passwordsaver.app

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.facebook.stetho.Stetho
import com.shivichu.passwordsaver.BuildConfig
import com.shivichu.passwordsaver.database.AppDatabase
import com.shivichu.passwordsaver.repository.TodoRepository
import com.shivichu.passwordsaver.retrofit.api.ApiInterface
import com.shivichu.passwordsaver.retrofit.base.RetrofitNetwork
import com.shivichu.passwordsaver.retrofit.interceptor.NetworkConnectionInterceptor
import com.shivichu.passwordsaver.viewModel.ToDoViewModel
import com.shivichu.passwordsaver.viewModel.common.ViewModelFactory
import com.sof.retail.viewModel.common.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Companion.lazy
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber
import timber.log.Timber.DebugTree

class AppController : Application(), KodeinAware {

    companion object {
        lateinit var instance : AppController
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Stetho.initializeWithDefaults(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    override val kodein = lazy {
        import(androidXModule(this@AppController))
        bind<Kodein>() with singleton{ kodein }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind<ApiInterface>() with singleton { RetrofitNetwork.getApiInterface(instance()) }
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
        //====================================================================================================
        //View Model
        //====================================================================================================
        bindViewModel<ToDoViewModel>() with provider { ToDoViewModel(instance()) }
        //====================================================================================================
        //Repository
        //====================================================================================================
        bind() from singleton { TodoRepository(instance(), instance()) }
    }
}