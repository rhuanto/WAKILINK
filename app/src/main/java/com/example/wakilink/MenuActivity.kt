package com.example.wakilink

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivity : AppCompatActivity() {
    private lateinit var cameraButton: Button
    private lateinit var translationText: EditText
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var characterImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        initializeViews()
        setupBottomNavigation()
        setupCameraButton()

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    private fun initializeViews() {
        cameraButton = findViewById(R.id.cameraButton)
        translationText = findViewById(R.id.translationText)
        bottomNav = findViewById(R.id.bottomNavigation)
        characterImageView = findViewById(R.id.characterImageView)
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    if (isUserLoggedIn()) {
                        startActivity(Intent(this, DashboardActivity::class.java))
                    }
                    true
                }
                R.id.navigation_calibration -> {
                    if (isUserLoggedIn()) {
                        startActivity(Intent(this, CalibrationActivity::class.java))
                    }
                    true
                }
                R.id.navigation_history -> {
                    if (isUserLoggedIn()) {
                        startActivity(Intent(this, ChatHistoryActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setupCameraButton() {
        cameraButton.setOnClickListener {
            if (checkCameraPermission()) {
                startCamera()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    private fun startCamera() {
        // Implementar la lógica de la cámara y reconocimiento de señas
    }

    private fun isUserLoggedIn(): Boolean {
        // Implementar verificación de login
        return false
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }
} 