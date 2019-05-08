package com.example.runoutnotifier

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.getSystemService
import com.example.runoutnotifier.model.MyItem
import com.example.runoutnotifier.model.MyItemDatabse
import java.util.*

class MyApp : Application() {
    companion object {
        lateinit var db : MyItemDatabse
    }


}