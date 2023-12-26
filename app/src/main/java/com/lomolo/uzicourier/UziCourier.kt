package com.lomolo.uzicourier

import android.app.Application

class UziCourierApp: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultContainer(this)
    }
}