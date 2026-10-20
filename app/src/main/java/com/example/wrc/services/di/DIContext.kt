package com.example.wrc.services.di
import kotlin.reflect.KClass

class DIContext(build: DIScope.() -> Unit) {
    @PublishedApi
    internal val services: Map<KClass<*>,()-> Any> = DIScope(this).apply(build).registrations

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(type: KClass<T>): T {
        return services[type]!!() as T
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T: Any> get():T {
        return services[T::class]!!() as T
    }
}