package com.rcmkt.redux.sample.network

import io.reactivex.Single
import retrofit2.http.GET

interface Api {

    @GET("strings")
    fun getStrings(): Single<List<String>>
}