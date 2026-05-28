package com.example.appbangiay.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbangiay.model.TinNhanChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@Composable
fun ManHinhChat() {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Bạn cần đăng nhập để chat")
        }
        return
    }

    val uid = user.uid

    val db = FirebaseFirestore.getInstance()

    var tinNhanNhap by remember { mutableStateOf("") }
    var danhSachTinNhan by remember { mutableStateOf<List<TinNhanChat>>(emptyList()) }

    LaunchedEffect(uid) {
        db.collection("chats")
            .document(uid)
            .collection("messages")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                danhSachTinNhan = snapshot.documents.map { doc ->
                    TinNhanChat(
                        id = doc.id,
                        text = doc.getString("text") ?: "",
                        sender = doc.getString("sender") ?: "",
                        senderName = doc.getString("senderName") ?: "",
                        createdAt = doc.getLong("createdAt") ?: 0L,
                        seen = doc.getBoolean("seen") ?: false
                    )
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFF064C8C)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Chat với HoangShoes",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(danhSachTinNhan) { tin ->
                val laUser = tin.sender == "user"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (laUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .background(
                                if (laUser) Color(0xFF064C8C) else Color.White,
                                RoundedCornerShape(14.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = tin.text,
                            color = if (laUser) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .background(Color.White)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = tinNhanNhap,
                onValueChange = { tinNhanNhap = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nhập tin nhắn...") },
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    val noiDung = tinNhanNhap.trim()
                    if (noiDung.isEmpty()) return@IconButton

                    val now = System.currentTimeMillis()

                    val chatRef = db.collection("chats").document(uid)
                    val messageRef = chatRef.collection("messages").document()

                    val message = hashMapOf(
                        "text" to noiDung,
                        "sender" to "user",
                        "senderName" to (user.displayName ?: "Người dùng"),
                        "createdAt" to now,
                        "seen" to false
                    )

                    val chatInfo = hashMapOf(
                        "userId" to uid,
                        "userName" to (user.displayName ?: "Người dùng"),
                        "userEmail" to (user.email ?: ""),
                        "userAvatar" to (user.photoUrl?.toString() ?: ""),
                        "lastMessage" to noiDung,
                        "lastSender" to "user",
                        "updatedAt" to now
                    )

                    messageRef.set(message)
                        .addOnSuccessListener {
                            chatRef.set(chatInfo, SetOptions.merge())
                            tinNhanNhap = ""
                            Toast.makeText(context, "Đã gửi", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Lỗi gửi: ${e.message}", Toast.LENGTH_LONG).show()
                            Log.e("CHAT_DEBUG", "Lỗi gửi tin nhắn", e)
                        }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = Color(0xFF064C8C)
                )
            }
        }
    }
}