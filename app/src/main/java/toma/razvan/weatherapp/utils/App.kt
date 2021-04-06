package toma.razvan.weatherapp.utils

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import toma.razvan.weatherapp.di.networkModule
import toma.razvan.weatherapp.di.viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger(Level.ERROR)

            modules(viewModelModule + networkModule)
        }
    }
}