package com.rcmkt.redux.sample.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.rcmkt.redux.sample.R
import com.rcmkt.redux.sample.di.DI
import com.rcmkt.redux.sample.main.redux.MainState
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    private val recyclerViewAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        DI.app.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initViews()

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.state.observe(this, Observer { state ->
            render(state)
        })
    }

    private fun initViews() {
        recycler_view.adapter = recyclerViewAdapter
    }

    private fun render(state: MainState) {
        if (state.isLoading) {
            progress_bar.isVisible = true
        } else {
            progress_bar.isVisible = false
            state.data?.let(recyclerViewAdapter::update)
        }
    }
}
