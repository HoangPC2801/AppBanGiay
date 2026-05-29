package com.example.appbangiay.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.appbangiay.MainActivity
import com.example.appbangiay.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
            ?: message.data["title"]
            ?: "HoangShoes"

        val body = message.notification?.body
            ?: message.data["body"]
            ?: "Bạn có thông báo mới"

        val type = message.data["type"] ?: ""
        val orderId = message.data["order_id"] ?: ""

        showNotification(title, body, type, orderId)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val user = com.google.firebase.auth.FirebaseAuth
            .getInstance()
            .currentUser ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                com.example.appbangiay.network.KetNoiServer.api.luuFcmToken(
                    com.example.appbangiay.model.FcmTokenRequest(
                        firebaseUid = user.uid,
                        token = token
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showNotification(
        title: String,
        body: String,
        type: String,
        orderId: String
    ) {
        val channelId = "hoangshoes_channel"

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("type", type)
            putExtra("order_id", orderId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val manager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "HoangShoes Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}