package com.practica.tp1android

data class Expense(
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    var isPaid: Boolean,
    val isFixed: Boolean,
    val isUrgent: Boolean
)