package com.example.kolo2assignment.character_model

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Results>,
    val total: Int
)