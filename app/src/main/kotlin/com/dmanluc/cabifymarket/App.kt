package com.dmanluc.cabifymarket

import android.app.Application
import com.dmanluc.cabifymarket.di.createApiModule
import org.koin.android.ext.android.startKoin

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoinDi()
    }

    private fun setupKoinDi() {
        startKoin(this, listOf(createApiModule(BuildConfig.BASE_URL)))
    }

}