package com.example.wrc.services

import kotlin.collections.minusAssign
import kotlin.collections.plusAssign

class Event<T: Any> : IEvent<T> {
    private val dataListeners = mutableListOf<(T) -> Unit>()

    override fun subscribe(listener: (T) -> Unit): EventToken {
        dataListeners += listener
        return EventToken { unsubscribe(listener) }
    }

    override fun unsubscribe(listener: (T) -> Unit) {
        dataListeners -= listener
    }
    fun invoke(signal:T): Unit = dataListeners.forEach { it.invoke(signal) }
}
interface IEvent<T: Any>{
    fun subscribe(listener: (T) -> Unit): EventToken
    fun unsubscribe(listener: (T) -> Unit)
}

class EventToken(
    private val onUnsubscribe: () -> Unit
) {
    private var isUnsubscribed = false

    fun unsubscribe() {
        if (isUnsubscribed)
            return

        isUnsubscribed = true
        onUnsubscribe()
    }
}

