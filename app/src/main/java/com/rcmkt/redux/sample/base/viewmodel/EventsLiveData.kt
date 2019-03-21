package com.rcmkt.redux.sample.base.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import com.rcmkt.redux.sample.base.redux.Event
import java.util.*

class EventsLiveData : MutableLiveData<Queue<Event>>() {

    @MainThread
    fun offer(event: Event) {
        val queue = value ?: LinkedList()
        queue.add(event)
        value = queue
    }
}