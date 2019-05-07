/* A harmadik labor DatePickerDialogFragment osztály alapján*/


package com.example.runoutnotifier

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.net.sip.SipSession
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import java.util.*

class DatePickerDialogFragment : DialogFragment() {

    private val SelectedDate = Calendar.getInstance()

    private lateinit var listener: DateListener

    private val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(datePicker: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            // Setting the new date
            SelectedDate.set(Calendar.YEAR, year)
            SelectedDate.set(Calendar.MONTH, monthOfYear)
            SelectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            listener.onDateSelected(buildDateText())

            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SelectedDate.time = Date(System.currentTimeMillis())
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(
            requireContext(),
            dateSetListener,
            SelectedDate.get(Calendar.YEAR),
            SelectedDate.get(Calendar.MONTH),
            SelectedDate.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = if (targetFragment != null) {
                targetFragment as DateListener
            } else {
                activity as DateListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    private fun buildDateText(): String {
        val dateString = StringBuilder()

        dateString.append(SelectedDate.get(Calendar.YEAR))
        dateString.append(".")
        dateString.append(SelectedDate.get(Calendar.MONTH) + 1)
        dateString.append(".")
        dateString.append(SelectedDate.get(Calendar.DAY_OF_MONTH))
        dateString.append(".")

        return dateString.toString()
    }

    interface DateListener {
        fun onDateSelected(date: String)
    }
}