package com.rcmkt.redux.sample.di

import android.content.Context

object DI {

    lateinit var app: AppComponent

    fun init(context: Context) {
        app = DaggerAppComponent
            .builder()
            .context(context)
            .build()
    }
}