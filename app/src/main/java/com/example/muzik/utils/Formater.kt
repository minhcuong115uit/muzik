package com.example.muzik.utils

class Formater {
    companion object{
        fun formatDuration(durationMs: Long): String {
            val seconds = (durationMs / 1000).toInt()
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return if (minutes > 0) {
                String.format("%d:%02d", minutes, remainingSeconds)
            } else {
                String.format("0:%02d", remainingSeconds)
            }
        }
    }
}