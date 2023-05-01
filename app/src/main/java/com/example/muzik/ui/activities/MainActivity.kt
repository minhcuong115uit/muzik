package com.example.muzik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.muzik.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navView: BottomNavigationView

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_home -> {
                // Xử lý khi người dùng chọn item "Home"
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_profile_user -> {
                // Xử lý khi người dùng chọn item "Profile User"
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_search -> {
                // Xử lý khi người dùng chọn item "Search"
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_library -> {
                // Xử lý khi người dùng chọn item "Library"
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_upload_music -> {
                // Xử lý khi người dùng chọn item "Upload Music"
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView = findViewById(R.id.bottom_nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}