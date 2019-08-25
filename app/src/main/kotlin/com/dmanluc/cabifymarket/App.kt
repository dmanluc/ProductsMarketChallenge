package com.dmanluc.cabifymarket

import android.app.Application
import com.dmanluc.cabifymarket.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoinDi()
    }

    private fun setupKoinDi() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    createRemoteModule(BuildConfig.BASE_URL),
                    repositoryModule,
                    localModule,
                    marketFeatureModule,
                    marketCheckoutModule
                )
            )
        }
    }

}