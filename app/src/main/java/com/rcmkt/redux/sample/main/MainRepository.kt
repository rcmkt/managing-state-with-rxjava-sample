package com.rcmkt.redux.sample.main

import com.rcmkt.redux.sample.network.Api
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val api: Api
) {

    fun getStrings(): Observable<List<String>> {
        return api.getStrings()
            .toObservable()
    }

    fun sendString(string: String) = Completable.complete()
}