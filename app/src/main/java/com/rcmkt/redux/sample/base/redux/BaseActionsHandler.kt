package com.rcmkt.redux.sample.base.redux

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

abstract class BaseActionsHandler<S : State> {

    abstract fun getActionsHandlers(
            shared: Observable<Action>,
            state: Observable<S>,
            onAction: (Action) -> Unit,
            onEvent: (Event) -> Unit
    ): List<Observable<Action>>

    fun bindHandlers(
            actions: Observable<Action>,
            state: Observable<S>,
            onAction: (Action) -> Unit,
            onEvent: (Event) -> Unit
    ): ObservableSource<Action> {
        return actions.publish { shared ->
            Observable.merge(
                    getActionsHandlers(shared, state, onAction, onEvent)
                            .plus(shared)
            )
        }
    }

    inline fun <reified A : Action> Observable<Action>.bind(handler: ObservableTransformer<A, Action>): Observable<Action> {
        return ofType(A::class.java).compose(handler)
    }
}