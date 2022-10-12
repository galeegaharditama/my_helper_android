package com.galih.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * State: A type that describes the data your feature needs to perform its logic and render its UI.
 *      The state is persistence during feature lifetime.
 * Effect: Similar to state but it is not persistence such as navigation, show toast, show snackbar.
 * Action: A type that represents all of the actions that cause the state of the application to
 *      change such as user actions, notifications, event sources and more.
 * Environment: A type that holds any dependencies the feature needs, such as
 *      API clients, analytics clients, etc to decouple between UI layer and Data layer.
 */
abstract class StatefulViewModel<STATE, EFFECT, ACTION, CONTRACT>(
  initialState: STATE,
  protected val contract: CONTRACT
) : ViewModel() {

  private val _state = MutableStateFlow(initialState)

  private val _effect: MutableStateFlow<EFFECT?> = MutableStateFlow(null)

  val state: StateFlow<STATE> = _state.asStateFlow()

  val effect: StateFlow<EFFECT?> = _effect.asStateFlow()

  abstract fun dispatch(action: ACTION)

  protected suspend fun setState(newState: STATE.() -> STATE) {
    _state.update(newState)
  }

  protected suspend fun setEffect(newEffect: EFFECT) {
    _effect.update { newEffect }
  }

  fun resetEffect() {
    _effect.update { null }
  }

  private fun stateValue(): STATE {
    return state.value
  }
}

@Composable
fun <STATE, EFFECT, ACTION, CONTRACT> StatefulViewModel<STATE, EFFECT, ACTION, CONTRACT>.handleEffect(
  handle: (EFFECT) -> Unit
) {
  val effect by this.effect.collectAsEffect()
  LaunchedEffect(effect) {
    effect?.let {
      handle(it)
      this@handleEffect.resetEffect()
    }
  }
}

@Composable
fun <T : R, R> Flow<T>.collectAsEffect(): State<R?> = collectAsState(initial = null)
