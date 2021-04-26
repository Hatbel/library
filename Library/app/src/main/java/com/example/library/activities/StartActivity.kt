package com.example.library.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.library.R

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar?.hide()
    }
}