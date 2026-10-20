package com.example.wrc.services


import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class EventBus(build: EventBusScope.() -> Unit) {
    val events:Map<KClass<*>, Event<*>> = EventBusScope().apply(build).events

    inline fun <reified T: Any> getEvent(): Event<T>{
        return  events[T::class] as Event<T>
    }
}
class EventBusScope {
    val events: MutableMap<KClass<*>, Event<*>> = mutableMapOf()

    inline fun <reified T: Any> event(){
        events[T::class] = Event<T>()
    }
}