package com.rcmkt.redux.sample.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcmkt.redux.sample.base.viewmodel.ViewModelFactory
import com.rcmkt.redux.sample.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(viewModel: MainViewModel): ViewModel
}