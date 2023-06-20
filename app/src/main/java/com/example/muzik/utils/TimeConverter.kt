package com.example.muzik.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class TimeConverter {
    companion object {

            fun convertTimestampToString(timestamp: Timestamp, format: String = "'lúc' HH:mm dd/MM/yyyy"): String {
                val date = timestamp.toDate()
                val dateFormat = SimpleDateFormat(format, Locale.getDefault())
                return dateFormat.format(date)
            }
    }
}