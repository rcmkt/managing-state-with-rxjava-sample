package com.rcmkt.redux.sample.di

import android.content.Context
import com.rcmkt.redux.sample.main.MainActivity
import com.rcmkt.redux.sample.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(appContext: Context): Builder

        fun build(): AppComponent
    }
}