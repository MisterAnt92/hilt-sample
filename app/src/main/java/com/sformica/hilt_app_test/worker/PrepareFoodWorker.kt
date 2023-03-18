package com.sformica.hilt_app_test.worker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * Worker
 *
 * @constructor
 */
@HiltWorker
open class PrepareFoodWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    companion object {
        val TAG: String = PrepareFoodWorker::class.java.simpleName

        val TIME_UNIT_INTERVAL = TimeUnit.HOURS
        const val REPEAT_INTERVAL: Long = 2

        private const val MAX_NUMBER_OF_RETRY = 10
    }


    init {
        Log.d(TAG, "Initialized $TAG worker")
    }

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Work started, do nothing...")
            return Result.Success()

        } catch (e: Exception) {
            if (runAttemptCount < MAX_NUMBER_OF_RETRY) {
                Log.e(TAG, "Work failed! It will be retried later. Attempt: $runAttemptCount", e)
                Result.retry()

            } else {
                Log.e(TAG, "Work failed for unknown reason!", e)
                Result.retry()
            }
        }
    }
}