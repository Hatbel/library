package com.example.library.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.library.R
import com.example.library.modules.DeepLinkManager
import com.example.library.modules.SessionManager
import com.google.android.material.navigation.NavigationView
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
    private val sessionManager: SessionManager by inject()
    private val linkManger: DeepLinkManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val navController = findNavController(R.id.navFragment)
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)
        val id = linkManger.manageLink(intent)
        if (id != -1) {
            navController.navigate(id)
        } else if (!sessionManager.isUserIn()) {
            val destination = ActivityNavigator(this).createDestination()
                .setIntent(Intent(this, StartActivity::class.java))
            ActivityNavigator(this).navigate(destination, null, null, null)
        }
    }
}
