package com.sformica.hilt_app_test

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.*
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.sformica.hilt_app_test.worker.PrepareFoodWorker
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/***
 * Test for PrepareFoodWorker
 *
 * https://gist.github.com/pfmaggi/fe2d069fb9c9b9c6ac3582b2e0a1e646
 *
 * @property targetContext Context
 * @property configuration Configuration
 * @property workManager WorkManager
 * @property instantTaskExecutorRule InstantTaskExecutorRule
 */
@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PrepareFoodWorkerTest {

    private lateinit var targetContext: Context
    private lateinit var configuration: Configuration
    private lateinit var workManager: WorkManager

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        configuration = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(targetContext, configuration)
        // Getting instance
        workManager = WorkManager.getInstance(targetContext)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testWorker() {
        // Create request
        val request = OneTimeWorkRequestBuilder<PrepareFoodWorker>()
            .build()
        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        workManager.enqueue(request).result.get()
        // Get WorkInfo
        val workInfo = workManager.getWorkInfoById(request.id).get()
        // Assert
        MatcherAssert.assertThat(workInfo.state, `is`(WorkInfo.State.RUNNING))
        MatcherAssert.assertThat(
            workInfo.tags.elementAt(0),
            `is`("com.sformica.hilt_app_test.worker.PrepareFoodWorker")
        )
    }
}

/**
 * RefreshLogSenderWorkerTestable
 *
 * @property coroutineContext CoroutineDispatcher
 * @constructor
 */
class RefreshLogSenderWorkerTestable(context: Context, params: WorkerParameters) :
    PrepareFoodWorker(context, params) {

    override val coroutineContext = SynchronousExecutor().asCoroutineDispatcher()

    override suspend fun doWork(): Result = runBlocking {
        super.doWork()
    }
}