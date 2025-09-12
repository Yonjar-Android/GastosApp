package com.example.gastosapp.data.database.entities

data class MonthlyTotalDb(
    val month: String,  // "01".."12"
    val total: Double?
)
