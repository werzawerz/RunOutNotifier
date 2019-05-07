package com.example.runoutnotifier

import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import com.example.runoutnotifier.model.MyItem
import kotlinx.android.synthetic.main.new_item.*
import java.util.*

class NewItemFragment : DialogFragment(), DatePickerDialogFragment.DateListener {

    private lateinit var listener: NewItemListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = if (targetFragment != null) {
                targetFragment as NewItemListener
            } else {
                activity as NewItemListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.new_item, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spnItemType.adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            listOf("Gramm", "Kilogramm", "Liter", "Piece", "Packet", "Box", "Other")
        )

        enterDate.text = " - "

        enterDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnNewItemOK.setOnClickListener {
            val type = when (spnItemType.selectedItemPosition) {
                0 -> MyItem.Type.Gramm
                1 -> MyItem.Type.Kilogramm
                2 -> MyItem.Type.Liter
                3 -> MyItem.Type.Piece
                4 -> MyItem.Type.Packet
                5 -> MyItem.Type.Box
                6 -> MyItem.Type.Other
                else -> MyItem.Type.Other
            }

            val yearStr = Calendar.getInstance().get(Calendar.YEAR).toString()
            val monthStr = Calendar.getInstance().get(Calendar.MONTH).toString()
            val dayStr = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
            val currentTime = yearStr + "." + monthStr + "." + dayStr


            val newItem = MyItem(
                etItemName.text.toString(),
                currentTime,
                enterDate.text.toString(),
                etItemQuantity.text.toString().toDouble(),
                type,
                false
            )

            listener.onItemCreated(newItem)
            dismiss()
        }

        btnNewItemCancel.setOnClickListener {
            dismiss()
        }


    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialogFragment()
        datePicker.setTargetFragment(this, 0)
        datePicker.show(fragmentManager, "Calendar Dab")
    }


    interface NewItemListener {
        fun onItemCreated(item : MyItem)
    }

    override fun onDateSelected(date: String) {
        enterDate.text = date
    }
}