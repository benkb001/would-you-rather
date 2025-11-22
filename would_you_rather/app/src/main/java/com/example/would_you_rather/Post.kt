package com.example.would_you_rather

data class Post(
    val post_id: String,
    val question: String,
    val option1: String,
    val option2: String,
    val option1Count: Integer,
    val option2Count: Integer,
    val author: String
)
