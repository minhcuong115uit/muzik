package com.example.muzik.utils

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.*
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController

@BindingAdapter(value = ["selectedValue", "selectedValueAttrChanged"], requireAll = false)
fun bindSpinnerData(
    spinner: Spinner,
    newValue: String?,
    inverseBindingListener: InverseBindingListener
) {
    // Thiết lập OnItemSelectedListener cho Spinner
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            // Khi giá trị được chọn thay đổi, ta gọi inverseBindingListener để thông báo cho Data Binding biết rằng giá trị đã thay đổi
            inverseBindingListener.onChange()
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    // Nếu giá trị mới được truyền vào khác null, ta sẽ đặt lại giá trị được chọn trong Spinner
    newValue?.let {
        val adapter = spinner.adapter as? ArrayAdapter<String>
        val position = adapter?.getPosition(it) ?: 0
        spinner.setSelection(position, false)
    }
}

@InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
fun captureSelectedValue(spinner: Spinner): String? {
    return spinner.selectedItem as? String
}
