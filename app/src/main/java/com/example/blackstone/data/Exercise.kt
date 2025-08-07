package com.example.blackstone.data

import java.io.Serializable

data class Exercise(
    val name: String,
    val unit: String,
    val goal: Float,
    var current: Float,
    val description: String,
    val imageResId: Int
) : Serializable