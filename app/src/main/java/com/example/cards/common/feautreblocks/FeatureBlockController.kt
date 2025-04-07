package com.example.cards.common.feautreblocks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow

public data class FeatureBlockUiEvent @PublishedApi internal constructor(
    val featureBlockId: String,
    val event: Any,
)

public abstract class FeatureBlockController<UiState, UiEvent> {

    protected val featureBlockScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

    public abstract val state: StateFlow<UiState>

    @Suppress("UNCHECKED_CAST")
    internal fun event(event: Any) {
        (event as? UiEvent)
            ?.let {
                onEvent(event)
            } ?: throw IllegalArgumentException(
            "Некорректный тип UiEvent для контроллера ${this::class.simpleName}"
        )
    }

    public open fun onEvent(event: UiEvent) {
        // По умолчанию не обрабатывает никакие события
    }

    public open fun onDestroy() {
        // По умолчанию ничего не делает
    }

    internal fun destroy() {
        onDestroy()
        featureBlockScope.cancel()
    }
}
