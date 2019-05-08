package com.example.runoutnotifier.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.runoutnotifier.model.Converters.BooleanConverter
import com.example.runoutnotifier.model.Converters.EnumTypeConverter

@Database(entities = [MyItem::class], version = 3, exportSchema = false)
@TypeConverters(EnumTypeConverter::class, BooleanConverter::class)
abstract class MyItemDatabse : RoomDatabase() {

    abstract fun itemDao() : MyItemDao
}