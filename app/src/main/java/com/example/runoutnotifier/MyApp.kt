package com.example.runoutnotifier

import android.app.Application
import android.arch.persistence.room.Room
import com.example.runoutnotifier.model.MyItemDatabse

class MyApp : Application() {
    companion object {
        lateinit var db : MyItemDatabse
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, MyItemDatabse::class.java, "db")
            .build()
    }
}