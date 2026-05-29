package com.example.appbangiay.ui.chat

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbangiay.model.TinNhanChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.appbangiay.ui.components.CartIconWithBadge
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun ManHinhChat(
    soLuongGioHang: Int,
    chuyenSangGioHang: () -> Unit,
    chuyenSangChiTiet: (Int) -> Unit,
    sanPhamId: Int? = null,
    tenSanPham: String? = null,
    giaSanPham: Int? = null,
    anhSanPham: String? = null
) {
    var daGuiSanPham by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(sanPhamId) {
        daGuiSanPham = false
    }
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

    // Quản lý trạng thái cuộn của danh sách tin nhắn
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // SỬA LỖI 1: Thay LaunchedEffect bằng DisposableEffect để dọn dẹp Listener khi thoát màn hình
    DisposableEffect(uid) {
        val listener = db.collection("chats")
            .document(uid)
            .collection("messages")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CHAT_DEBUG", "Lỗi lắng nghe: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    danhSachTinNhan = snapshot.documents.map { doc ->
                        TinNhanChat(
                            id = doc.id,
                            text = doc.getString("text") ?: "",
                            sender = doc.getString("sender") ?: "",
                            senderName = doc.getString("senderName") ?: "",
                            createdAt = doc.getLong("createdAt") ?: 0L,
                            seen = doc.getBoolean("seen") ?: false,
                            messageType = doc.getString("messageType") ?: "text",
                            productId = doc.getLong("productId")?.toInt(),
                            productName = doc.getString("productName"),
                            productImage = doc.getString("productImage"),
                            productPrice = doc.getLong("productPrice")?.toInt()
                        )
                    }
                }
            }

        // Hủy lắng nghe dữ liệu ngầm khi component bị hủy
        onDispose {
            listener.remove()
        }
    }

    // SỬA LỖI 2: Tự động cuộn xuống cuối cùng mỗi khi danh sách tin nhắn có sự thay đổi
    LaunchedEffect(danhSachTinNhan.size) {
        if (danhSachTinNhan.isNotEmpty()) {
            listState.animateScrollToItem(danhSachTinNhan.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .imePadding() // SỬA LỖI 3: Thêm imePadding để màn hình đẩy lên khi bật bàn phím ảo
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFF064C8C))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = "Chat với HoangShoes",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Start
            )

            CartIconWithBadge(
                soLuong = soLuongGioHang,
                onClick = chuyenSangGioHang,
                iconColor = Color.White
            )
        }

        if (
            sanPhamId != null &&
            !daGuiSanPham
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(
                    1.dp,
                    Color(0xFFE0E0E0)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = anhSanPham,
                        contentDescription = null,
                        modifier = Modifier.size(62.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Bạn muốn hỏi về sản phẩm này?",
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = tenSanPham ?: "",
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1
                        )

                        Text(
                            text = "${formatMoneyChat(giaSanPham ?: 0)}đ",
                            color = Color(0xFF064C8C),
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    IconButton(
                        onClick = {

                            guiTinNhanSanPham(
                                db = db,
                                uid = uid,
                                userName = user.displayName ?: "Người dùng",
                                sanPhamId = sanPhamId,
                                tenSanPham = tenSanPham,
                                giaSanPham = giaSanPham,
                                anhSanPham = anhSanPham
                            )

                            daGuiSanPham = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Gửi sản phẩm",
                            tint = Color(0xFF064C8C),
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            daGuiSanPham = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }

        LazyColumn(
            state = listState, // Gán state cuộn
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
                    if (tin.messageType == "product") {
                        Card(
                            modifier = Modifier
                                .width(220.dp)
                                .clickable {
                                    tin.productId?.let {
                                        chuyenSangChiTiet(it)
                                    }
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Color(0xFF2196F3)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp)
                            ) {
                                AsyncImage(
                                    model = tin.productImage,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentScale = ContentScale.Fit
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = tin.productName ?: "Sản phẩm",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black,
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${formatMoneyChat(tin.productPrice ?: 0)}đ",
                                    color = Color(0xFFE53935),
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Xem chi tiết",
                                    color = Color(0xFF2196F3),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    } else {
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

                    // Reset field ngay khi nhấn nút cho mượt mà
                    tinNhanNhap = ""

                    val now = System.currentTimeMillis()
                    val chatRef = db.collection("chats").document(uid)
                    val messageRef = chatRef.collection("messages").document()

                    val message = hashMapOf(
                        "text" to noiDung,
                        "sender" to "user",
                        "senderName" to (user.displayName ?: "Người dùng"),
                        "createdAt" to now,
                        "seen" to false,

                        "productId" to sanPhamId,
                        "productName" to tenSanPham,
                        "productImage" to anhSanPham,
                        "productPrice" to giaSanPham
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
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Lỗi gửi: ${e.message}", Toast.LENGTH_LONG).show()
                            Log.e("CHAT_DEBUG", "Lỗi gửi tin nhắn", e)
                        }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Gửi",
                    tint = Color(0xFF064C8C)
                )
            }
        }
    }
}

fun guiTinNhanSanPham(
    db: FirebaseFirestore,
    uid: String,
    userName: String,
    sanPhamId: Int?,
    tenSanPham: String?,
    giaSanPham: Int?,
    anhSanPham: String?
) {
    if (sanPhamId == null) return

    val now = System.currentTimeMillis()

    val chatRef = db.collection("chats").document(uid)
    val messageRef = chatRef.collection("messages").document()

    val text = "Tôi muốn hỏi về sản phẩm này"

    val message = hashMapOf(
        "text" to text,
        "sender" to "user",
        "senderName" to userName,
        "createdAt" to now,
        "seen" to false,
        "messageType" to "product",
        "productId" to sanPhamId,
        "productName" to tenSanPham,
        "productImage" to anhSanPham,
        "productPrice" to giaSanPham
    )

    val chatInfo = hashMapOf(
        "userId" to uid,
        "userName" to userName,
        "lastMessage" to text,
        "lastSender" to "user",
        "updatedAt" to now
    )

    messageRef.set(message)
        .addOnSuccessListener {
            chatRef.set(chatInfo, SetOptions.merge())
        }
}

fun formatMoneyChat(value: Int): String {
    return "%,d".format(value).replace(",", ".")
}