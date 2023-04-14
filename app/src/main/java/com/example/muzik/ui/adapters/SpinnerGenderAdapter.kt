package com.example.muzik.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.muzik.R

class SpinnerGenderAdapter (private val context: Context, private val genderList: List<String>) : BaseAdapter(){

    override fun getCount(): Int {
        return genderList.size;
    }
    fun getPosition(gender: String):Int{
        return genderList.indexOf(gender);
    }
    override fun getItem(position: Int): String {
        return genderList[position];
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.spinner_gender_item,parent,false);
        var gender: TextView = view.findViewById(R.id.spinner_tv_item);
        var circleIcon: ImageView = view.findViewById(R.id.spinner_item_circle_icon);
        gender.text = genderList[position];
        circleIcon.visibility = View.GONE;
        return view;
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.spinner_gender_item,parent,false);
        var gender: TextView = view.findViewById(R.id.spinner_tv_item);
        gender.text = genderList[position];
        return view;
    }
}