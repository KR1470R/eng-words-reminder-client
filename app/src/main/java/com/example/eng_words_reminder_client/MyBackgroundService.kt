package com.example.eng_words_reminder_client

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

class MyBackgroundService : Service() {

    private val TAG = "MyBackgroundService"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                Log.d(TAG, "Code executed at: ${System.currentTimeMillis()}")
            }
        }, getDelayUntilMidnight(), 24 * 60 * 60 * 1000)

        return START_STICKY
    }

    private fun getDelayUntilMidnight(): Long {
        val currentTime = Calendar.getInstance()
        val now = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }

        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 44)
        }

        return midnight.timeInMillis - now.timeInMillis
    }
}
