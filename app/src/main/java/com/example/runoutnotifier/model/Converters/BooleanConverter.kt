package com.example.runoutnotifier.model.Converters

import android.arch.persistence.room.TypeConverter
import com.example.runoutnotifier.model.MyItem

class BooleanConverter {

    @TypeConverter
    fun toBool(value : Int?) : Boolean {
       return(value!=0)
    }

    @TypeConverter
    fun toInteger(b : Boolean ) : Int? {
        if(b==false)
            return 0
        else
            return 1
    }
}