package com.example.eng_words_reminder_client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eng_words_reminder_client.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startPushService()
    }

    private fun startPushService() {
        /*        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        startService(serviceIntent)*/
    }
}