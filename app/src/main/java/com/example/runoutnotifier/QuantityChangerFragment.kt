package com.example.runoutnotifier

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.runoutnotifier.ItemDetailActivity.Companion.selectedItem
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.android.synthetic.main.quantity_changer.*

class QuantityChangerFragment : DialogFragment()  {

    private lateinit var listener: QuantityChangedListener
    var rebuy = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = if (targetFragment != null) {
                targetFragment as QuantityChangedListener
            } else {
                activity as QuantityChangedListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.quantity_changer, container, false)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btnQuantityChangedOK.setOnClickListener {
            listener.onQuantityChanged(etQauntityChange.text.toString().toDouble(), rebuy)
            dismiss()
        }
        btnQuantityChangedCancel.setOnClickListener() {
            dismiss()
        }
    }


    interface QuantityChangedListener {
        fun onQuantityChanged(d : Double, rebuy: Boolean)
    }
}