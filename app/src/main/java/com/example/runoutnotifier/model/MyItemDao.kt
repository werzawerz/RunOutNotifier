package com.example.runoutnotifier.model

import android.arch.persistence.room.*


@Dao
interface MyItemDao {
    @Insert
    fun insertAll(vararg params : MyItem)

    @Query("SELECT * FROM MyItem ORDER BY dueDate")
    fun getAll() : List<MyItem>

    @Query("SELECT * FROM MyItem WHERE name = :key")
    fun getElementByKey(key : String) : MyItem

    @Update
    fun update(myItem : MyItem) : Int

    @Delete
    fun delete(param : MyItem)

}