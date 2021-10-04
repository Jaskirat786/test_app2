package com.example.kolo2assignment.comic_model

data class Character(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)