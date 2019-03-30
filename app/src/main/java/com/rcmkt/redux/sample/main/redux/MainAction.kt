package com.rcmkt.redux.sample.main.redux

import com.rcmkt.redux.sample.base.redux.Action

sealed class MainAction : Action {

    object GetData : MainAction()

    object Loading : MainAction()

    object Success : MainAction()

    data class DataLoaded(val data: List<String>) : MainAction()

    data class ErrorLoading(val error: Throwable) : MainAction()

    class SendString(val string: String) : MainAction()
}