package com.github.eylulnc.walkmunich

import android.app.Application
import com.github.eylulnc.walkmunich.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WalkMunichApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WalkMunichApp)
            modules(appModule)
        }
    }
}