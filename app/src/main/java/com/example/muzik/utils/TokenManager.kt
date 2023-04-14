package com.example.muzik.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager private constructor (applicationContext: Context){
    private val context: Context = applicationContext;
    private val tokenPrefs: SharedPreferences = context.getSharedPreferences(TOKEN_PREFS, Context.MODE_PRIVATE);
        companion object{
            private const val TOKEN_PREFS = "token_prefs"
            private const val KEY_ACCESS_TOKEN = "access_token"
            private const val KEY_REFRESH_TOKEN = "refresh_token"

            @Volatile
            private var instance: TokenManager? = null
            fun getInstance(applicationContext: Context): TokenManager {
                return instance ?: synchronized(this) {
                    instance ?: TokenManager(applicationContext).also { instance = it }
                }
            }
        }

    fun saveAccessToken(accessToken: String) {
        tokenPrefs.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply()
    }

    fun saveRefreshToken(refreshToken: String) {
        tokenPrefs.edit().putString(KEY_REFRESH_TOKEN, refreshToken).apply()
    }

    fun getAccessToken(): String? {
        return tokenPrefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return tokenPrefs.getString(KEY_REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        tokenPrefs.edit().remove(KEY_ACCESS_TOKEN).remove(KEY_REFRESH_TOKEN).apply()
    }
}