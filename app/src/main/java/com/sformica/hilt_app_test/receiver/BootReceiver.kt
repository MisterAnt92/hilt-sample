package com.sformica.hilt_app_test.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.sformica.hilt_app_test.worker.PrepareFoodWorker
import dagger.hilt.android.AndroidEntryPoint

/**
 * Boot receiver
 *
 * @constructor Create empty Boot receiver
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    companion object {
        private val TAG: String = BootReceiver::class.java.simpleName
        private const val ACTION = "android.intent.action.BOOT_COMPLETED"
    }

    /**
     * On receive
     *
     * @param context
     * @param intent
     */
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION) {
            Log.d(TAG, "BootReceiver active!")
            setupTimeWorker(WorkManager.getInstance(context))
        }
    }

    /**
     * Setup log sender time worker to run periodically in case of internet connection available.
     *
     * @param workManager
     */
    private fun setupTimeWorker(workManager: WorkManager) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()


        val work = PeriodicWorkRequestBuilder<PrepareFoodWorker>(
            PrepareFoodWorker.REPEAT_INTERVAL,
            PrepareFoodWorker.TIME_UNIT_INTERVAL
        )
            .setBackoffCriteria(BackoffPolicy.LINEAR, 2L, PrepareFoodWorker.TIME_UNIT_INTERVAL)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            PrepareFoodWorker.TAG,
            ExistingPeriodicWorkPolicy.UPDATE,
            work
        )
        Log.d(TAG, "WorkManager with ${PrepareFoodWorker.TAG} is ready to start periodically!")
    }
}