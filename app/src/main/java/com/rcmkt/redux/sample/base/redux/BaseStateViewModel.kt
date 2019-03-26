package com.rcmkt.redux.sample.base.redux

import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.rcmkt.redux.sample.base.viewmodel.BaseViewModel
import com.rcmkt.redux.sample.internal.SchedulersProvider

abstract class BaseStateViewModel<S : State>(
    private val actionsHandler: BaseActionsHandler<S>,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    abstract val initialState: S

    val state = MutableLiveData<S>()

    private val actionsSource = PublishRelay.create<Action>()

    private val stateSource = BehaviorRelay.create<S>()

    abstract fun reduce(state: S, action: Action): S

    fun initStateProcessor() {
        actionsSource
            .observeOn(schedulersProvider.io())
            .compose { actions -> actionsHandler.bindHandlers(actions, stateSource.hide(), ::sendAction, ::sendEvent) }
            .scan(initialState) { state, action -> reduce(state, action) }
            .distinctUntilChanged()
            .subscribe(stateSource::accept)
            .autoDispose()

        stateSource
            .subscribe(state::postValue)
            .autoDispose()
    }

    fun sendAction(action: Action) = actionsSource.accept(action)

    protected fun sendEvent(event: Event) = events.offer(event)
}