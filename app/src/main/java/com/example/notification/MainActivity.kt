package com.example.notification

import android.accessibilityservice.GestureDescription
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.example.notification.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var notificationManager : NotificationManager? = null //untuk inisialisasi
    private var CHANNEL_ID = "channel_id"

    private lateinit var countDownTimer: CountDownTimer //lateinit utk memberi tahu bahwasan nya itu countdowntimer
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Register channel ke dalam system
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(CHANNEL_ID, "CountDown", "This is Description")

        binding.btnStart.setOnClickListener {
            countDownTimer.start()
        }

        countDownTimer = object : CountDownTimer(10000, 1000){
            override fun onTick(p0: Long) {
                //masukin text dari string
                binding.timer.text = getString(R.string.time_reamining, p0/1000 )
            }

            override fun onFinish() {
                displayNotification()
            }

        }

    }

    private fun displayNotification(){
        val notificationID = 20
        val intent = Intent(this, HalamanActivity2::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentTitle("pak haji")
            .setContentText("Tugas Kampus")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\""))

        //untuk menampilkan notifikasi
        with(NotificationManagerCompat.from(this)) {
            notify(notificationID, builder.build())
        }
    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String){
        //validasi notif akan dibuat jika version sdk 16+ (keatas)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}