package com.example.wrc.services.di

import kotlin.reflect.KClass

class DIScope(val di:DIContext) {
    val registrations = mutableMapOf<KClass<*>, () -> Any>()

    inline fun <reified T : Any> single(noinline factory: () -> T) {
        val instance = factory()
        val instanceLazy:T by lazy {
            if(instance is IService){
                instance.inject(di)
            }
            instance
        }
        registrations[T::class] = { instanceLazy}
    }

    inline fun <reified T : Any> single(instance: T) {
        val instanceLazy:T by lazy {
            if(instance is IService){
                instance.inject(di)
            }
            instance
        }
        registrations[T::class] = { instanceLazy }
    }

    inline fun <reified T : Any> transient(factory: ()-> T){
        registrations[T::class] = {
            val instance:T = factory()
            if(instance is IService){
                instance.inject(di)
            }
            instance
        }
    }
}