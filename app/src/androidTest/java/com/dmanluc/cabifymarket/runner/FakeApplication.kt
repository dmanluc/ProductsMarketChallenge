package com.dmanluc.cabifymarket.runner

import android.app.Application

/**
 * We use a separate [Application] for tests to prevent initializing release modules.
 * On the contrary, we will provide inside our tests custom [Module] directly.
 */
class FakeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}