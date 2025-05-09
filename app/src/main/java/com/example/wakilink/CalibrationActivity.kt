package com.example.wakilink

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CalibrationActivity : AppCompatActivity() {
    private lateinit var startCalibrationButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibration)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        
        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        startCalibrationButton = findViewById(R.id.startCalibrationButton)
        statusTextView = findViewById(R.id.statusTextView)
    }

    private fun setupListeners() {
        startCalibrationButton.setOnClickListener {
            if (checkCameraPermission()) {
                startCalibration()
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

    private fun startCalibration() {
        statusTextView.text = "Calibrando..."
        // Aquí se implementaría la lógica de calibración
        // Por ejemplo, capturar varios frames de la cámara y procesarlos
        // para ajustar los parámetros del modelo de reconocimiento
        
        // Simulación de calibración exitosa
        Toast.makeText(this, "Calibración completada", Toast.LENGTH_SHORT).show()
        statusTextView.text = "Calibración completada"
        
        // Guardar estado de calibración en Firestore
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .update("isCalibrated", true)
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }
} 