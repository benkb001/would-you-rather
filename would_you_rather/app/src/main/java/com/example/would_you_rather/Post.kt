package com.example.would_you_rather

data class Post(
    val post_id: String = "",
    val question: String = "",
    val option1: String = "",
    val option2: String = "",
    val option1Count: Int = 0,
    val option2Count: Int = 0,
    val author: String = ""
)
