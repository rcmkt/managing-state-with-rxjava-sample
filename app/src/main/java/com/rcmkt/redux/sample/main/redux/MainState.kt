package com.rcmkt.redux.sample.main.redux

import com.rcmkt.redux.sample.base.redux.State
import com.xwray.groupie.kotlinandroidextensions.Item

data class MainState(
    val data: List<Item>?,
    val isLoading: Boolean,
    val error: Throwable?
) : State