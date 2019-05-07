package com.example.runoutnotifier

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RebuyFragment : DialogFragment() {

    private lateinit var listener : RebuyListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = if (targetFragment != null) {
                targetFragment as RebuyListener
            } else {
                activity as RebuyListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.quantity_changer, container,false)
        return view
    }

    interface RebuyListener {
        fun onQuantityChanged(d : Double)
        fun onDateSelected(date: String)
    }
}