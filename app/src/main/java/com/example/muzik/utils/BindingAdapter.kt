package com.example.muzik.utils

import android.R
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.navigation.NavController

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("shortValue")
    fun setShortValue(view: EditText, value: Short?) {
        view.setText(value?.toString() ?: "")
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "shortValue", event = "shortValueAttrChanged")
    fun getShortValue(view: EditText): Short? {
        val valueString = view.text.toString()
        return if (valueString.isEmpty()) null else valueString.toShort()
    }

    @JvmStatic
    @BindingAdapter("shortValueAttrChanged")
    fun setShortValueAttrChanged(view: EditText, listener: InverseBindingListener?) {
        if (listener != null) {
            view.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    listener.onChange()
                }
            }
        }
    }
    @JvmStatic
    @BindingAdapter("app:items")
    fun Spinner.setItems(items: List<String>) {
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }

}