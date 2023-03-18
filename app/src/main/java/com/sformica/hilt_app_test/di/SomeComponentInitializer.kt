package com.sformica.hilt_app_test.di

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import dagger.hilt.android.EarlyEntryPoints

/**
 * Initializer form some component
 *
 * @property pizzaDependency EatDependency
 * @property pastaDependency EatDependency
 */
class SomeComponentInitializer : Initializer<Unit> {

    companion object {
        private val TAG: String = SomeComponentInitializer::class.java.simpleName
    }

    private lateinit var pizzaDependency: EatDependency
    private lateinit var pastaDependency: EatDependency

    override fun create(context: Context) {
        // Manual Hilt injection
        pizzaDependency =
            EarlyEntryPoints.get(context, InitializerEntryPoint::class.java).eatPizzaDependency()
        pastaDependency =
            EarlyEntryPoints.get(context, InitializerEntryPoint::class.java).eatPastaDependency()
        Log.d(
            TAG,
            "SomeComponentInitializer-initializing code... eatDependency: $pizzaDependency and pastaDependency:$pastaDependency"
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
