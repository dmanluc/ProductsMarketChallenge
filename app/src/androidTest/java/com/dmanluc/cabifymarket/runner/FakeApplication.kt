package com.dmanluc.cabifymarket.runner

import android.app.Application

/**
 * Specific test [Application] for tests to prevent initializing DI modules with a custom runner.
 * On the contrary, we will provide inside our tests custom DI modules directly.
 */
class FakeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}