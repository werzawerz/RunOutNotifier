package com.example.runoutnotifier

//import android.app.Fragment
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.runoutnotifier.model.MyItem
import com.example.runoutnotifier.model.MyItemDatabse
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.android.synthetic.main.item_row.*
import java.util.*


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class MyItemDetailFragment: Fragment(), DatePickerDialogFragment.DateListener, View.OnClickListener, QuantityChangerFragment.QuantityChangedListener {

    /**
     * The MyItem content this fragment is presenting.
     */
    private var selectedItem: MyItem? = null
    private lateinit var contextus : Context

    companion object {
        val ARG_ITEM_ID = "Item_Id"
    }

    private lateinit var listener: MyItemDetailFragment.UpdateItemListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        contextus = context!!;

        try {
            listener = if (targetFragment != null) {
                targetFragment as MyItemDetailFragment.UpdateItemListener
            } else {
                activity as MyItemDetailFragment.UpdateItemListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        return rootView
    }

    fun setItem(item : MyItem) {
        selectedItem = item
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auxs  = "You have ${selectedItem?.quantity.toString()} ${selectedItem?.type.toString()}"
        tvDetailQuantity.text = auxs
        tvDetailDueDate.text = selectedItem?.dueDate.toString()

        val btnChangeDueTime = view.findViewById<Button>(R.id.btnChangeDueTime)
        btnChangeDueTime.setOnClickListener(this)
        val btnChangeQuantity = view.findViewById<Button>(R.id.btnChangeQuantity)
        btnChangeQuantity.setOnClickListener(this)
        val btnRebuy = view.findViewById<Button>(R.id.btnRebuy)
        btnRebuy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnChangeDueTime -> {
                val datePicker = DatePickerDialogFragment()
                datePicker.setTargetFragment(this, 0)
                datePicker.show(fragmentManager, "datepicker")


            }
            R.id.btnChangeQuantity -> {
                val quantityChanger = QuantityChangerFragment()
                quantityChanger.setTargetFragment(this, 0)
                quantityChanger.show(fragmentManager, "dab")
            }
            R.id.btnRebuy -> {
                Log.d("ChangeDueTime", "megnyomtad")
                val quantityChanger = QuantityChangerFragment()
                quantityChanger.setTargetFragment(this, 0)
                quantityChanger.show(fragmentManager, "dab")
            }
        }
    }

    override fun onDateSelected(date: String) {
        Log.d("ChangeDueTime", "valasztottal")
        tvDetailDueDate.text = date
        selectedItem?.dueDate = date;
        listener.onItemUpdated(selectedItem)

        val dateParts : List<String> = date.split('.')
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, dateParts[0].toInt())
        c.set(Calendar.MONTH, dateParts[1].toInt()-1)
        c.set(Calendar.DAY_OF_MONTH, dateParts[2].toInt())

        startAlarm(c,selectedItem!!)

    }

    override fun onQuantityChanged(d: Double) {
        val auxs  = "You have ${d.toString()} ${selectedItem?.type.toString()}"
        tvDetailQuantity.text = auxs
        selectedItem?.quantity = d;
        listener.onItemUpdated(selectedItem)
    }

    fun startAlarm(c : Calendar, item : MyItem) {
        val alarmManager = contextus.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(contextus, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(contextus, selectedItem.hashCode(), intent, 0)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)

    }

    interface UpdateItemListener {
        fun onItemUpdated(item : MyItem?)
    }

}
