package com.example.runoutnotifier

import android.R
import android.content.Context.NOTIFICATION_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.support.v4.app.NotificationCompat

//forrás : https://codinginflow.com/tutorials/android/alarmmanager

class NotificationHelper(base: Context) : ContextWrapper(base) {

    private var mManager: NotificationManager? = null

    lateinit var contentIntent : PendingIntent

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }

            return mManager
        }

    val channelNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle("RunOutNotifier")
            .setContentText("OOOOOPS! It looks like you run out of something. Check out, what you need to buy")
            .setSmallIcon(R.drawable.ic_lock_idle_alarm)
            .setContentIntent(contentIntent)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
            createBackActivity()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)

        manager!!.createNotificationChannel(channel)
    }

    private fun createBackActivity() {
        val notificationIntent = Intent(this, ItemListActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

        contentIntent = PendingIntent.getActivity(this,
            NOTIFICATION_ID,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT)
    }

    companion object {
        val channelID = "channelID"
        val channelName = "Channel Name"
        private const val NOTIFICATION_ID = 101
    }
}