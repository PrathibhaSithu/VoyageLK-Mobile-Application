package com.example.madminiproject.models

data class Post(
    var postId: String = "",
    var image: String = "",
    var title: String = "",
    var location: String = "",
    var author: String = "",
    var views: Int = 0 ,
    var content: String = "",
    val userId: String = ""
)
