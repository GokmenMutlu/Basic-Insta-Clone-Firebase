package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.model

import com.google.firebase.Timestamp


data class Post(
    var postId: String = "", // Postun Firestore'daki ID'si
    val downloadImageUrl: String = "",
    val title: String = "",
    val comment: String = "",
    val userEmail: String = "",
    val date: Timestamp? = null
)