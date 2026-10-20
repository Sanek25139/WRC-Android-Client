package com.example.wrc.vms

import androidx.lifecycle.ViewModel
import com.example.wrc.services.StateVar
import com.example.wrc.services.StateVarScope

abstract class FormViewModel : ViewModel() {

    private val validations = mutableListOf<()-> Boolean>()

    protected fun <T : Any> state(
        initial: T,
        build: StateVarScope<T>.() -> Unit = {}
    ): StateVar<T> {
        return StateVar(initial, build).also {
            validations += it::validate
        }
    }

    protected fun validateAll(): Boolean =
        validations.map { it() }.all { it }
}