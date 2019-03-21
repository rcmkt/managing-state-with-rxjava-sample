package com.rcmkt.redux.sample.main.redux

import com.rcmkt.redux.sample.base.redux.Action

sealed class MainAction : Action {

    object GetData : MainAction()

    object Loading : MainAction()

    object Success : MainAction()

    class DataLoaded(val data: List<String>) : MainAction()

    class ErrorLoading(val error: Throwable) : MainAction()

    class SendString(val string: String) : MainAction()
}