package com.example.blackstone.data

data class MissionItem(
    val name: String,
    val target: Int,
    val unit: String,
    val description: String,
    var completed: Int
)