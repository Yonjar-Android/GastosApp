package com.example.gastosapp.data.database.entities

data class ExpenseWithDetails(
    val id: Long,
    val description: String,
    val cost: Double,
    val payment: Double,
    val date: Long,
    val clientId: Long,
    val productId: Long,
    val clientFirstName: String,
    val clientLastName: String,
    val productName: String,
    val status: Boolean
    )
