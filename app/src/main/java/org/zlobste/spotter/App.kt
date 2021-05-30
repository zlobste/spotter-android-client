package org.zlobste.spotter

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.zlobste.spotter.identity.CredentialsProvider
import org.zlobste.spotter.identity.CredentialsProviderImpl
import org.zlobste.spotter.util.ConnectivityInterceptor
import org.zlobste.spotter.util.ToastManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))

        //utils
        bind() from provider { ToastManager(instance()) }

        //credentials on preferences
        bind<CredentialsProvider>() with provider { CredentialsProviderImpl(this@App) }

        //network
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptor() }
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

    }
}