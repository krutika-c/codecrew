package com.codecrew.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnResource = findViewById<Button>(R.id.btnOpenResourceProvider)
        val btnDashboard = findViewById<Button>(R.id.btnOpenDashboard)

        btnResource.setOnClickListener {
            startActivity(Intent(this, ResourceProviderActivity::class.java))
        }

        btnDashboard.setOnClickListener {
            startActivity(Intent(this, DashBoardActivity::class.java))
        }
    }
}
