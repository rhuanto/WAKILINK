package com.example.wakilink

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ChatHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_history)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        
        recyclerView = findViewById(R.id.chatHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        loadChatHistory()

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadChatHistory() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("chats")
                .whereEqualTo("userId", currentUser.uid)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    val chatList = documents.map { doc ->
                        ChatHistoryItem(
                            id = doc.id,
                            timestamp = doc.getTimestamp("timestamp")?.toDate() ?: Date(),
                            location = doc.getString("location") ?: "Desconocido",
                            rating = doc.getDouble("rating")?.toFloat() ?: 0f
                        )
                    }
                    recyclerView.adapter = ChatHistoryAdapter(chatList) { chatId, rating ->
                        updateChatRating(chatId, rating)
                    }
                }
        }
    }

    private fun updateChatRating(chatId: String, rating: Float) {
        db.collection("chats")
            .document(chatId)
            .update("rating", rating)
    }
}

data class ChatHistoryItem(
    val id: String,
    val timestamp: Date,
    val location: String,
    val rating: Float
)

class ChatHistoryAdapter(
    private val chats: List<ChatHistoryItem>,
    private val onRatingChanged: (String, Float) -> Unit
) : RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.dateText)
        val locationText: TextView = view.findViewById(R.id.locationText)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        
        holder.dateText.text = dateFormat.format(chat.timestamp)
        holder.locationText.text = chat.location
        holder.ratingBar.rating = chat.rating
        
        holder.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            onRatingChanged(chat.id, rating)
        }
    }

    override fun getItemCount() = chats.size
} 