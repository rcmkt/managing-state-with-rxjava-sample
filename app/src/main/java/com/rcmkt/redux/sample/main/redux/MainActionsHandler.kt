package com.rcmkt.redux.sample.main.redux

import androidx.lifecycle.Transformations.map
import com.rcmkt.redux.sample.base.redux.Action
import com.rcmkt.redux.sample.base.redux.BaseActionsHandler
import com.rcmkt.redux.sample.base.redux.Event
import com.rcmkt.redux.sample.main.MainRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class MainActionsHandler @Inject constructor(
    private val repository: MainRepository
) : BaseActionsHandler<MainState>() {

    override fun getActionsHandlers(
        shared: Observable<Action>,
        state: Observable<MainState>,
        onAction: (Action) -> Unit,
        onEvent: (Event) -> Unit
    ): List<Observable<Action>> {
        return listOf(
            shared.bind(createGetDataHandler()),
            shared.bind(createSendStringHandler())
        )
    }

    private fun createGetDataHandler(): ObservableTransformer<MainAction.GetData, Action> {
        return ObservableTransformer { actions ->
            actions.switchMap {
                repository.getStrings()
                    .map<Action>(MainAction::DataLoaded)
                    .startWith(MainAction.Loading)
                    .onErrorReturn(MainAction::ErrorLoading)
            }
        }
    }

    private fun createSendStringHandler(): ObservableTransformer<MainAction.SendString, Action> {
        return ObservableTransformer { actions ->
            actions.switchMap { action ->
                repository.sendString(action.string)
                    .toObservable<Action>()
                    .map<Action> { MainAction.Success }
                    .startWith(MainAction.Loading)
                    .onErrorReturn(MainAction::ErrorLoading)
            }
        }
    }
}