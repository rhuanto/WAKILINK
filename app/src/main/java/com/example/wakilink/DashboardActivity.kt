package com.example.wakilink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.TextView
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {
    private lateinit var totalUsersCard: CardView
    private lateinit var activeUsersCard: CardView
    private lateinit var localUsersCard: CardView
    private lateinit var totalUsersText: TextView
    private lateinit var activeUsersText: TextView
    private lateinit var localUsersText: TextView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        db = FirebaseFirestore.getInstance()
        initializeViews()
        loadMetrics()

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }
    }

    private fun initializeViews() {
        totalUsersCard = findViewById(R.id.totalUsersCard)
        activeUsersCard = findViewById(R.id.activeUsersCard)
        localUsersCard = findViewById(R.id.localUsersCard)
        totalUsersText = findViewById(R.id.totalUsersCount)
        activeUsersText = findViewById(R.id.activeUsersCount)
        localUsersText = findViewById(R.id.localUsersCount)
    }

    private fun loadMetrics() {
        // Cargar total de usuarios
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                val totalUsers = documents.size()
                totalUsersText.text = totalUsers.toString()
            }

        // Cargar usuarios activos
        db.collection("users")
            .whereEqualTo("isActive", true)
            .get()
            .addOnSuccessListener { documents ->
                val activeUsers = documents.size()
                activeUsersText.text = activeUsers.toString()
            }

        // Cargar usuarios locales (ejemplo: dentro de 10km)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            db.collection("users")
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener { documents ->
                    val localUsers = documents.size()
                    localUsersText.text = localUsers.toString()
                }
        }
    }
} 