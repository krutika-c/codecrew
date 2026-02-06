package com.example.add_friends

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CONTACTS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkContactsPermission()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { loadFragment(HomeFragment()); true }
                R.id.nav_eye -> { loadFragment(EyeFragment()); true }
                R.id.nav_list -> { loadFragment(ListFragment()); true }
                R.id.nav_friends -> { loadFragment(FriendsContainerFragment()); true }
                R.id.nav_pencil -> { loadFragment(WriteFragment()); true }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            loadFragment(FriendsContainerFragment()) // default
        }
    }

    private fun checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadFragment(FriendsContainerFragment())
        } else {
            Toast.makeText(this, "Contacts permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}