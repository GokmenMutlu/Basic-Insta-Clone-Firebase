package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {  // Insta da gibi post'un ne zaman paylaşıldığını yazdırma işlemi.

    fun formatTimeAgo(timestamp: Timestamp): String {
        val postDate = timestamp.toDate()  // Firestore Timestamp'ı Date formatına çevir
        val now = Date()  // Şu anki zaman

        val diffInMillis = now.time - postDate.time

        // Zaman farkını saat, gün, hafta bazında al
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

        return when {
            seconds < 60 -> "Az önce"
            minutes < 60 -> "$minutes dakika önce"
            hours < 24 -> "$hours saat önce"
            days < 7 -> "$days gün önce"
            days < 30 -> "${days / 7} hafta önce"
            else -> {  // 4 haftayı geçtiyse doğrudan tarihi yazdır
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                "Paylaşıldığı tarih: ${sdf.format(postDate)}"
            }
        }
    }

}