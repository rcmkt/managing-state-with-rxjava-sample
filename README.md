# Managing state with RxJava
Базовый пример показывающий, как реактивно определять и управлять State в приложении.

## Как начать?
* Описать State
* Определить набор Action
* Добавить ActionsHandlers
* Добавить Reducer

## Пример: сделать загрузку данных и отобразить

### Описание State

```kotlin
data class State(
        val screenState: ScreenState<List<Item>>,
        val data: List<Domain>
)
```

### Описание набора Action
```kotlin
sealed class Action {

    object GetData : Action()
    
    class DataLoaded(val data: List<Data>) : Action()

    object Loading : Action()

    class Error(val error: Throwable) : Action()
}
```
В этом примере определяется `Action.DataLoaded`, чтобы передать в Reducer данные, которые загрузились и обновить на его основе State.

### Добавление ActionsHandlers
Определить класс *FeatureName***ActionsHandlers** внутри него, на примере `Action.GetData`, добавляем соответсвующий Handler:

```kotlin
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
```
Внутри базового класса **BaseActionsHandler** определена операция расщепления потока Actions:
```kotlin
actions.publish { shared ->
            Observable.merge(
                    getActionsHandlers(shared, state, onAction, onEvent)
                            .plus(shared)
            )
        }
```

### Добавление Reducer
Для начала необходимо проинициализировать начальное состояние во ViewModel: 
```kotlin
override val initialState = MainState(
        data = null,
        isLoading = false,
        error = null
    )
```
Помимо этого ответственность за подготовку нового State, в ответ на пришедший Action, лежит на ViewModel:
```kotlin
fun reduce(state: State, action: Action): State {
        return when (action) {
            is Action.Loading -> state.copy(screenState = Loading())
            is Action.DataLoaded -> state.copy(screenState = Content(), data = action.data)
            else -> state
        }
}
```

Однако, если такой логики становится через чур много, уместно вынести это в отдельный класс и назвать его с префиксом *Reducer*

### F.A.Q.

**Q:** Мне нужно запушить Event, как это сделать?

**A:** В методе `bindHandlers` передается лямба `onEvent()`, которую можно передать в нужный **Handler** и вызвать в цепочке с помощью `.doOnNext { }`

