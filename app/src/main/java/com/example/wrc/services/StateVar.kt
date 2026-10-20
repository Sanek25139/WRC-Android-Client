package com.example.wrc.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class StateVar<T: Any>(instance:T,build:StateVarScope<T>.() -> Unit) {
    var value: T by mutableStateOf(instance)
    var isError: Boolean by mutableStateOf(false)
    var errorMsg: String by mutableStateOf("")
    val scope = StateVarScope<T>().apply(build)
    fun changeValue(newValue: T) {
        val error = scope.filters.firstOrNull { !it.check(newValue) }

        if (error != null) {
            errorMsg = error.errorMessage
            isError = true
            return
        }

        value = newValue
        errorMsg = ""
        isError = false
    }
    fun validate(): Boolean {
        val error = scope.validations.firstOrNull { !it.check(value) }

        if (error != null) {
            errorMsg = error.errorMessage
            isError = true
            return false
        }

        isError = false
        errorMsg = ""
        return true
    }

}
class StateVarScope<T : Any> {

    val filters = mutableListOf<Rule<T>>()
    val validations = mutableListOf<Rule<T>>()

    fun validateRule(
        rule: (T) -> Boolean,
        errorMsg: String = "",
    ) {
        validations += Rule(rule, errorMsg)
    }

    fun validateFilter(
        rule: (T) -> Boolean,
        errorMsg: String = ""
    ) {
        filters += Rule(rule, errorMsg)
    }
    fun validateRule(

        errorMsg: String = "",
        rule: (T) -> Boolean
    ) {
        validations += Rule(rule, errorMsg)
    }

    fun validateFilter(
        errorMsg: String = "",
        rule: (T) -> Boolean
    ) {
        filters += Rule(rule, errorMsg)
    }
}
data class Rule<T>(
    val check: (T) -> Boolean,
    val errorMessage: String
)