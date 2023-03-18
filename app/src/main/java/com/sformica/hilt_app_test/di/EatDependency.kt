package com.sformica.hilt_app_test.di

import javax.inject.Inject

/**
 * Eat dependency
 *
 * @constructor Create empty Eat dependency
 */
class EatDependency @Inject constructor() {

    /**
     * Eat pizza dependency
     *
     */
    fun eatPizzaDependency() {}

    /**
     * Eat pasta dependency
     *
     */
    fun eatPastaDependency() {}
}
