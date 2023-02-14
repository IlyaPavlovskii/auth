package io.github.ilyapavlovskii.kmm.signin.sample

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

internal class SignInApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SignInApplication)
            modules(SignInComponent.modules)
        }
    }
}