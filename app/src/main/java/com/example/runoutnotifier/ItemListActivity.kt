package com.example.runoutnotifier

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.runoutnotifier.adapter.SimpleItemRecyclerViewAdapter

import com.example.runoutnotifier.model.MyItem
import com.example.runoutnotifier.model.MyItemDatabse
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import java.util.*
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Context
import android.support.v7.widget.PopupMenu
import android.widget.Toast
import com.example.runoutnotifier.MyApp.Companion.db
import kotlin.system.exitProcess


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), SimpleItemRecyclerViewAdapter.MyItemClickListener, NewItemFragment.NewItemListener,
MyItemDetailFragment.UpdateItemListener  {
        private lateinit var recycler: SimpleItemRecyclerViewAdapter
        //lateinit var db: MyItemDatabse

        override fun onClick(item: MyItem) {
            if (twoPane) {
                val fragment = MyItemDetailFragment()
                fragment.setItem(item)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                ItemDetailActivity.selectedItem = item
                val intent = Intent(this, ItemDetailActivity::class.java)
                ItemDetailActivity.db = db;
                startActivityForResult(intent, 1)
            }
        }

        override fun onLongClick(item : MyItem, view: View) {
            Log.d("jaj", "bemenetem")
            val popup = PopupMenu(this, view)
            popup.inflate(R.menu.menu_delete)
            popup.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.delete -> {
                        Thread {
                            db.itemDao().delete(item)
                        }.start()
                        recycler.removeItem(item)
                    }
                    R.id.hide -> {
                        item.hide = true
                        Thread {
                            db.itemDao().update(item)
                        }.start()
                        recycler.removeItem(item)
                    }
                }
                false;
            }
            popup.show()
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_options, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem?): Boolean {
            when(item?.itemId) {
                R.id.addNewItem  -> {
                    val todoCreateFragment = NewItemFragment()
                    todoCreateFragment.show(supportFragmentManager, "TAG")
                }
                R.id.showHidden -> {
                    var items : List<MyItem>
                    val changedItems : MutableList<MyItem> = mutableListOf()
                    Thread {
                        items = db.itemDao().getAll()
                        for(i in 0 until items.size) {
                            if(items[i].hide)
                                changedItems.add(items[i])
                            items[i].hide = false
                            db.itemDao().update(items[i])
                        }
                        this.runOnUiThread {
                            recycler.addAll(changedItems)
                        }
                    }.start()

                }
                R.id.deleteAll -> {
                    var items : List<MyItem>
                    Thread {
                        items = db.itemDao().getAll()
                        for(i in 0 until items.size) {
                            db.itemDao().delete(items[0])
                        }
                    }
                    recycler.removeAll()
                }
                R.id.exit -> {
                    exitProcess(0)
                }
            }

            return super.onOptionsItemSelected(item)
        }

        /**
         * Whether or not the activity is in two-pane mode, i.e. running on a tablet
         * device.
         */
        private var twoPane: Boolean = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_item_list)

            setSupportActionBar(toolbar)
            toolbar.title = title

            if (item_detail_container != null) {
                // The detail container view will be present only in the
                // large-screen layouts (res/values-w900dp).
                // If this view is present, then the
                // activity should be in two-pane mode.
                twoPane = true
            }

            fab.setOnClickListener {
                val todoCreateFragment = NewItemFragment()
                todoCreateFragment.show(supportFragmentManager, "TAG")
            }


            setupRecyclerView()
        }

        private fun setupRecyclerView() {

            db = Room.databaseBuilder(applicationContext, MyItemDatabse::class.java, "db")
                .build()
            recycler = SimpleItemRecyclerViewAdapter(this, db)
            recycler.itemClickListener = this
            Thread {
                recycler.addAll(db.itemDao().getAll())
            }.start()
            item_list.adapter = recycler

        }

        override fun onItemCreated(item: MyItem) {
            Thread {
                var have = false;
                val allItems : List<MyItem>
                allItems = db.itemDao().getAll()
                for(i in 0 until allItems.size) {
                    if(allItems[i].name==item.name) {
                        this.runOnUiThread {
                            Toast.makeText(this, "You have this item in the database.", Toast.LENGTH_LONG).show()
                        }
                        have = true;
                    }
                }
                if(!have) {
                    db.itemDao().insertAll(item)
                    this.runOnUiThread {
                        recycler.addItem(item)
                        val dateParts : List<String> = item.dueDate!!.split('.')
                        val c = Calendar.getInstance()
                        c.set(Calendar.YEAR, dateParts[0].toInt())
                        c.set(Calendar.MONTH, dateParts[1].toInt()-1)
                        c.set(Calendar.DAY_OF_MONTH, dateParts[2].toInt())
                        startAlarm(c, item)
                    }
                }
            }.start()

        }

        override fun onItemUpdated(item: MyItem?) {
            Thread {
                db.itemDao().update(item!!)
            }.start()

        }

        fun startAlarm(c : Calendar, item : MyItem) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlertReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, item.hashCode(), intent, 0)

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)

        }


}
