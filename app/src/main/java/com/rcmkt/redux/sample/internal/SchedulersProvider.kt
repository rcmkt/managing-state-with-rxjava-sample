package com.rcmkt.redux.sample.internal

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler

    fun computation(): Scheduler

    fun mainThread(): Scheduler
}