package com.rcmkt.redux.sample.ui.main

import com.nhaarman.mockitokotlin2.mock
import com.rcmkt.redux.sample.TrampolineSchedulersProvider
import com.rcmkt.redux.sample.ViewModelTest
import com.rcmkt.redux.sample.main.MainRepository
import com.rcmkt.redux.sample.main.MainViewModel
import com.rcmkt.redux.sample.main.redux.MainActionsHandler
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.IOException

class MainViewModelTest : ViewModelTest() {

    @Test
    fun `when request is successful - then data is not null`() {
        // Given
        val mainRepositoryStub = mock<MainRepository> {
            on { getStrings() }.thenReturn(Observable.just(emptyList()))
        }
        val viewModel = createViewModel(mainRepositoryStub)

        // When
        viewModel.loadStrings()

        // Then
        assertThat(viewModel.state.value!!.isLoading).isFalse()
        assertThat(viewModel.state.value!!.data).isNotNull
    }

    @Test
    fun `when request is failed - then error is not null`() {
        // Given
        val networkError = IOException("Network problem")
        val mainRepositoryStub = mock<MainRepository> {
            on { getStrings() }.thenReturn(Observable.error(networkError))
        }
        val viewModel = createViewModel(mainRepositoryStub)

        // When
        viewModel.loadStrings()

        // Then
        assertThat(viewModel.state.value!!.error).isEqualTo(networkError)
        assertThat(viewModel.state.value!!.isLoading).isFalse()
    }

    @Test
    fun `when request is loading - then show loading`() {
        // Given
        val mainRepositoryStub = mock<MainRepository> {
            on { getStrings() }.thenReturn(Observable.empty())
        }
        val viewModel = createViewModel(mainRepositoryStub)

        // When
        viewModel.loadStrings()

        // Then
        assertThat(viewModel.state.value!!.isLoading).isTrue()
    }

    private fun createViewModel(mockRepository: MainRepository): MainViewModel {
        val actionsHandlerStub = MainActionsHandler(mockRepository)

        return MainViewModel(
            actionsHandlerStub,
            TrampolineSchedulersProvider()
        )
    }
}