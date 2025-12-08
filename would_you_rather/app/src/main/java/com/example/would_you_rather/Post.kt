package com.example.would_you_rather

data class Post(
    val post_id: String,
    val question: String,
    val option1: String,
    val option2: String,
    val option1Count: Int,
    val option2Count: Int,
    val author: String
)
