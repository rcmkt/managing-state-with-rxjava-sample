package com.rcmkt.redux.sample.main

import com.rcmkt.redux.sample.base.redux.Action
import com.rcmkt.redux.sample.base.redux.BaseStateViewModel
import com.rcmkt.redux.sample.main.recycler.MainItem
import com.rcmkt.redux.sample.main.redux.MainAction
import com.rcmkt.redux.sample.main.redux.MainActionsHandler
import com.rcmkt.redux.sample.main.redux.MainState
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class MainViewModel @Inject constructor(
    actionsHandler: MainActionsHandler
) : BaseStateViewModel<MainState>(actionsHandler) {

    override val initialState = MainState(
        data = null,
        isLoading = false,
        error = null
    )

    init {
        initStateProcessor()
    }

    override fun reduce(state: MainState, action: Action): MainState {
        return when (action) {
            MainAction.Loading -> {
                state.copy(isLoading = true)
            }
            is MainAction.ErrorLoading -> {
                state.copy(isLoading = false, error = action.error)
            }
            is MainAction.DataLoaded -> {
                state.copy(isLoading = false, data = mapToUiData(action.data))
            }
            else -> state
        }
    }

    private fun mapToUiData(data: List<String>): List<Item> {
        return data.map { MainItem(it, ::sendAction) }
    }
}
