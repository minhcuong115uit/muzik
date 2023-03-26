package com.example.muzik.utils
import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

object ViewUtils {
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }
}