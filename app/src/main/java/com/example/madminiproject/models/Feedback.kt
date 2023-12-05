package com.example.madminiproject.models

data class Feedback(
    var feedbackId: String = "",
    var image: String = "",
    var email: String = "",
    var rating: Double = 0.0,
    var feedback: String = "",
    val author: String = "",
    val userId: String = ""
)