package com.sformica.hilt_app_test.di

import dagger.hilt.InstallIn
import dagger.hilt.android.EarlyEntryPoint
import dagger.hilt.components.SingletonComponent

/**
 * Initializer entry point
 *
 * @constructor Create empty Initializer entry point
 */
@EarlyEntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerEntryPoint {
    /**
     * @return EatDependency
     */
    fun eatPizzaDependency(): EatDependency

    /**
     * @return EatDependency
     */
    fun eatPastaDependency(): EatDependency
}
