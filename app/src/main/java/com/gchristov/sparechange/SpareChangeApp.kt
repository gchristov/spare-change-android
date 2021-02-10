package com.gchristov.sparechange

import android.app.Application
import com.gchristov.sparechange.di.networkModule
import com.gchristov.sparechange.di.repositoryModule
import com.gchristov.sparechange.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SpareChangeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDependencyInjection()
    }

    // Setup

    private fun setupDependencyInjection() {
        startKoin {
            androidContext(this@SpareChangeApp)
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}
