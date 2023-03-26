package com.example.muzik.listeners

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String?)
}
