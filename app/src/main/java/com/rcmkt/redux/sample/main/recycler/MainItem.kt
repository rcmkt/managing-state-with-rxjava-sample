package com.rcmkt.redux.sample.main.recycler

import com.rcmkt.redux.sample.R
import com.rcmkt.redux.sample.base.redux.Action
import com.rcmkt.redux.sample.main.redux.MainAction
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class MainItem(
    private val data: String,
    private val onClick: (Action) -> Unit
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.containerView.setOnClickListener {
            onClick.invoke(MainAction.SendString(data))
        }
    }

    override fun getLayout() = R.layout.item_main
}