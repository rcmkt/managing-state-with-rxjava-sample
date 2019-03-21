package com.rcmkt.redux.sample.base.redux

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.rcmkt.redux.sample.base.viewmodel.BaseViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers

abstract class BaseStateViewModel<S : State>(
    private val actionsHandler: BaseActionsHandler<S>
) : BaseViewModel() {

    abstract val initialState: S

    val state: LiveData<S>

    private val actionsSource = PublishRelay.create<Action>()

    private val stateSource = BehaviorRelay.create<S>()

    abstract fun reduce(state: S, action: Action): S

    init {
        state = LiveDataReactiveStreams.fromPublisher(stateSource.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun initStateProcessor() {
        actionsSource
            .observeOn(Schedulers.io())
            .compose { actions -> actionsHandler.bindHandlers(actions, stateSource.hide(), ::sendAction, ::sendEvent) }
            .scan(initialState) { state, action -> reduce(state, action) }
            .distinctUntilChanged()
            .subscribe(stateSource::accept)
            .autoDispose()
    }

    protected fun sendEvent(event: Event) = events.offer(event)

    protected fun sendAction(action: Action) = actionsSource.accept(action)
}