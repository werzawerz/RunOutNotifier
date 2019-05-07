package com.example.runoutnotifier.model.Converters

import android.arch.persistence.room.TypeConverter
import com.example.runoutnotifier.model.MyItem

class EnumTypeConverter {

    @TypeConverter
    fun toType(value : Int?) : MyItem.Type {
        return MyItem.Type.fromInt(value!!)
    }

    @TypeConverter
    fun toInteger(typ : MyItem.Type? ) : Int? {
        return typ?.enumValue;
    }
}