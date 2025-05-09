package com.example.wakilink.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wakilink.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.TextView

class DashboardFragment : Fragment() {
    private lateinit var totalUsersCount: TextView
    private lateinit var localUsersCount: TextView
    private lateinit var dailySessionsCount: TextView
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        totalUsersCount = view.findViewById(R.id.total_users_count)
        localUsersCount = view.findViewById(R.id.local_users_count)
        dailySessionsCount = view.findViewById(R.id.daily_sessions_count)

        loadDashboardData()
    }

    private fun loadDashboardData() {
        // Cargar total de usuarios conectados
        db.collection("users")
            .whereEqualTo("isOnline", true)
            .get()
            .addOnSuccessListener { documents ->
                totalUsersCount.text = documents.size().toString()
            }

        // Cargar usuarios en la localidad actual
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { userDoc ->
                    val userLocation = userDoc.getString("location")
                    if (userLocation != null) {
                        db.collection("users")
                            .whereEqualTo("location", userLocation)
                            .whereEqualTo("isOnline", true)
                            .get()
                            .addOnSuccessListener { documents ->
                                localUsersCount.text = documents.size().toString()
                            }
                    }
                }
        }

        // Cargar sesiones diarias
        val today = java.time.LocalDate.now().toString()
        db.collection("translations")
            .whereEqualTo("date", today)
            .get()
            .addOnSuccessListener { documents ->
                dailySessionsCount.text = documents.size().toString()
            }
    }
} 