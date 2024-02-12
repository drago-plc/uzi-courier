package com.lomolo.uzicourier

import android.app.Application
import com.lomolo.uzicourier.container.AppContainer
import com.lomolo.uzicourier.container.DefaultContainer

class UziCourierApp: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultContainer(this)
    }
}