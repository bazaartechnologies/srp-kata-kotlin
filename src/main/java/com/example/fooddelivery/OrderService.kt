package com.example.fooddelivery

class OrderService {
    private val orders = mutableMapOf<String, MutableMap<String, Any>>() // orderId -> { details }

    fun checkOrderItems(itemIds: List<String>){
        if (itemIds.isEmpty()) throw RuntimeException("Order must have at least one item.")
    }






}