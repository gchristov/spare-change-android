package com.gchristov.sparechange.ui.base

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<S : Any>(initialState: S) : ViewModel() {

    protected val disposables = CompositeDisposable()

    private val _state = MutableLiveData(initialState)

    val state: LiveData<S> get() = _state

    @MainThread
    protected fun setState(reducer: S.() -> S) {
        val currentState = _state.value
        val newState = currentState?.reducer()
        if (newState != currentState) {
            _state.value = newState
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
