package com.rcmkt.redux.sample.di

import com.rcmkt.redux.sample.internal.SchedulersProvider
import com.rcmkt.redux.sample.internal.SchedulersProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppModule {

    @Binds
    @Singleton
    fun provideSchedulersProvider(impl: SchedulersProviderImpl): SchedulersProvider

}