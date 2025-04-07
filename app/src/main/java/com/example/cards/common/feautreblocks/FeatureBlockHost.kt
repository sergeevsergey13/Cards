package com.example.cards.common.feautreblocks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.job

public data class FeatureBlockHostModel<T>(
    val state: T,
    val featureBlocksStates: Map<String, Any>,
)

public interface IFeatureBlockHost<T> {

    public fun hostState(
        initialState: T,
        state: Flow<T>,
        viewModelScope: CoroutineScope
    ): StateFlow<FeatureBlockHostModel<T>>

    public fun onFeatureBlockEvent(event: FeatureBlockUiEvent)
}

public class FeatureBlockHost<T>(
    private val featureBlockControllers: Map<String, FeatureBlockController<*, *>>,
) : IFeatureBlockHost<T> {

    private var _hostState: StateFlow<FeatureBlockHostModel<T>>? = null

    public override fun hostState(
        initialState: T,
        state: Flow<T>,
        viewModelScope: CoroutineScope
    ): StateFlow<FeatureBlockHostModel<T>> {
        if (_hostState != null) return _hostState.requireNotNull()

        initialize(viewModelScope)

        val hostState = featureBlockControllers
            .map { (key, controller) -> controller.state.map { key to it.requireNotNull() } }
            .let { featureBlocksStatesFlows ->
                combine(featureBlocksStatesFlows) { states -> states.associate { it } }
            }
            .combine(state) { featureBlocksStates, hostState ->
                FeatureBlockHostModel(
                    state = hostState,
                    featureBlocksStates = featureBlocksStates,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = FeatureBlockHostModel(
                    state = initialState,
                    featureBlocksStates = featureBlockControllers.mapValues {
                        it.value.state.value.requireNotNull()
                    },
                ),
            )

        _hostState = hostState
        return hostState
    }

    override fun onFeatureBlockEvent(event: FeatureBlockUiEvent) {
        featureBlockControllers[event.featureBlockId]?.event(event.event)
    }

    private fun initialize(viewModelScope: CoroutineScope) {
        viewModelScope
            .coroutineContext
            .job
            .invokeOnCompletion { featureBlockControllers.forEach { it.value.destroy() } }
    }
}

inline fun <reified T> T?.requireNotNull(): T = requireNotNull(this)
inline fun <reified T> Any.cast(): T = this as T
