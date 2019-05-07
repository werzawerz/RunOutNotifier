package com.example.runoutnotifier.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
class MyItem(
    @PrimaryKey
    val name : String,
    @ColumnInfo(name = "PurchaseDate")
    val purchaseDate : String,
    @ColumnInfo(name = "DueDate")
    var dueDate : String? = null,
    @ColumnInfo(name = "Qunatity")
    var quantity : Double,
    @ColumnInfo(name = "Type")
    val type : Type,
    @ColumnInfo(name = "Hide")
    var hide : Boolean
) {
    enum class Type(value : Int) {
        Gramm(0),
        Kilogramm(1),
        Liter(2),
        Piece(3),
        Packet(4),
        Box(5),
        Other(6);

        val enumValue = value;

        companion object {
            fun fromInt(x : Int) : Type {
                when(x) {
                    0 -> {
                        return Gramm
                    }
                    1 -> {
                        return Kilogramm
                    }
                    2 -> {
                        return Liter
                    }
                    3 -> {
                        return Piece
                    }
                    4 -> {
                        return Packet
                    }
                    5 -> {
                        return Box
                    }
                    6 -> {
                        return Other
                    }
                }
                return Other
            }
        }
    }

}