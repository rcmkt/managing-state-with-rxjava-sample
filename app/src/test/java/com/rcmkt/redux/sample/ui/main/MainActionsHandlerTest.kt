package com.rcmkt.redux.sample.ui.main

import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.mock
import com.rcmkt.redux.sample.TrampolineSchedulersProvider
import com.rcmkt.redux.sample.base.redux.Action
import com.rcmkt.redux.sample.main.MainRepository
import com.rcmkt.redux.sample.main.redux.MainAction
import com.rcmkt.redux.sample.main.redux.MainActionsHandler
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Test
import java.io.IOException

class MainActionsHandlerTest {

    private val schedulersProvider = TrampolineSchedulersProvider()

    @Test
    fun `when receive GetData - then expect sequence Loading, DataLoaded`() {
        // Given
        val mainRepositoryStub = mock<MainRepository> {
            on { getStrings() }.thenReturn(Observable.just(emptyList()))
        }
        val actionsHandler = MainActionsHandler(mainRepositoryStub)
        val actionsSource = PublishRelay.create<Action>()
        val testSequence = setOf<Action>(MainAction.Loading, MainAction.DataLoaded(emptyList()))
        val testObserver = TestObserver.create<Action>()

        actionsSource
            .observeOn(schedulersProvider.io())
            .compose { actions -> actionsHandler.bindHandlers(actions, mock(), mock(), mock()) }
            .subscribe(testObserver)

        actionsHandler.bindHandlers(actionsSource, mock(), mock(), mock()).test()

        // When
        actionsSource.accept(MainAction.GetData)

        // Then
        testObserver.assertValueSet(testSequence)
    }

    @Test
    fun `when receive GetData and error happens - then expect sequence Loading, ErrorLoading`() {
        // Given
        val networkError = IOException("Network problem")
        val mainRepositoryStub = mock<MainRepository> {
            on { getStrings() }.thenReturn(Observable.error(networkError))
        }
        val actionsHandler = MainActionsHandler(mainRepositoryStub)
        val actionsSource = PublishRelay.create<Action>()
        val testSequence = setOf<Action>(MainAction.Loading, MainAction.ErrorLoading(networkError))
        val testObserver = TestObserver.create<Action>()

        actionsSource
            .observeOn(schedulersProvider.io())
            .compose { actions -> actionsHandler.bindHandlers(actions, mock(), mock(), mock()) }
            .subscribe(testObserver)

        // When
        actionsSource.accept(MainAction.GetData)

        // Then
        testObserver.assertValueSet(testSequence)
    }
}