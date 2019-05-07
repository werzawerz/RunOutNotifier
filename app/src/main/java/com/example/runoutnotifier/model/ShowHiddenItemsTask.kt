package com.example.runoutnotifier.model

import android.os.AsyncTask
import com.example.runoutnotifier.MyApp
import com.example.runoutnotifier.adapter.SimpleItemRecyclerViewAdapter

class ShowHiddenItemsTask : AsyncTask<Void, SimpleItemRecyclerViewAdapter, Void>() {

    val changedItems : MutableList<MyItem> = mutableListOf()

    override fun doInBackground(vararg params: Void?): Void? {
        var items : List<MyItem>
        items = MyApp.db.itemDao().getAll()
        for(i in 0 until items.size) {
            if(items[i].hide)
                changedItems.add(items[i])
            items[i].hide = false
            MyApp.db.itemDao().update(items[i])
        }
        return null
    }

    override fun onProgressUpdate(vararg values: SimpleItemRecyclerViewAdapter?) {
        values[0]?.addAll(changedItems)
    }

}