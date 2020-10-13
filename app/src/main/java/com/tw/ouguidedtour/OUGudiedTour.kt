package com.tw.ouguidedtour

import android.app.Application
import timber.log.Timber

class OUGudiedTour: Application() {

    // Used to Init Timber logger
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}