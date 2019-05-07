package com.example.runoutnotifier.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.runoutnotifier.R
import com.example.runoutnotifier.model.MyItem
import com.example.runoutnotifier.model.MyItemDatabse
import kotlinx.android.synthetic.main.item_row.view.*
import java.util.*
import java.util.zip.Inflater

class SimpleItemRecyclerViewAdapter(
    private val context: Context,
    private val database: MyItemDatabse
) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    private val itemList = mutableListOf<MyItem>()
    lateinit var itemClickListener : MyItemClickListener
    private lateinit var rootView : View

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(parent: ViewHolder, position: Int) {
        val item = itemList[position]

        parent.item = item

        parent.tvDueDate.text = item.dueDate
        parent.tvQuantity.text = item.quantity.toString()
        parent.tvName.text = item.name
        parent.tvType.text = item.type.toString()

        var dateParts : List<String> = item.dueDate!!.split('.')
        var dueDate = Date(dateParts[0].toInt(), dateParts[1].toInt(), dateParts[2].toInt())
        if(dueDate < Calendar.getInstance().time) {
            rootView.setBackgroundColor(R.color.DarkRed)
        }

    }

    fun addAll(items: List<MyItem>) {
        //val size = itemList.size
        for(i in 0 until items.size step 1 ) {
            if(!items[i].hide)
                addItem(items[i])
        }
        //notifyItemRangeInserted(size, items.size)
    }

    fun addItem(item : MyItem) {
        itemList += item
        notifyDataSetChanged()
    }

    fun removeItem(item : MyItem) {
        itemList.remove(item)
        notifyDataSetChanged()
    }

    fun removeAll() {
        itemList.removeAll {
            true
        }
        notifyDataSetChanged()
    }

    interface MyItemClickListener {
        fun onClick(item: MyItem)
        fun onLongClick(item : MyItem, view : View)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDueDate: TextView = itemView.tvDueDate
        val tvType: TextView = itemView.tvType
        val tvName : TextView = itemView.tvName
        val tvQuantity : TextView = itemView.tvQuantity

        var item: MyItem? = null

        init {
            itemView.setOnClickListener {
                item?.let { item ->  itemClickListener.onClick(item) }
            }

            itemView.setOnLongClickListener { view ->
                item?.let { item -> itemClickListener.onLongClick(item!!, view) }
                true
            }
        }
    }

}