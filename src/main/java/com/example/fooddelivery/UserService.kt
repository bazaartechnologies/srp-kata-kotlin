package com.example.fooddelivery

class UserService {
    private val userBalances = mutableMapOf<String, Double>() // userId -> balance


    fun validateUserBalance(userId: String){
        if (!userBalances.containsKey(userId)) throw RuntimeException("User not found.")
    }
    fun addUser(userId: String, balance: Double) {
        userBalances[userId] = balance
    }
    fun checkUserBalance(userId: String, total: Double) {
        if (userBalances[userId]!! < total) throw RuntimeException("Insufficient balance.")
    }



}