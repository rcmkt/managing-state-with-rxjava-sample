package com.rcmkt.redux.sample

import android.app.Application
import com.rcmkt.redux.sample.di.DI

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DI.init(this)
    }
}