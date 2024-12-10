package com.example.fooddelivery;

import java.util.*


class FoodDeliverySystem {
    private val menu = mutableMapOf<String, Triple<String, Double, Int>>() // itemId -> (name, price, inventory)
    private val orders = mutableMapOf<String, MutableMap<String, Any>>() // orderId -> { details }
    private val userBalances = mutableMapOf<String, Double>() // userId -> balance
    private val riders = mutableListOf<String>() // List of available riders

    // Menu Operations
    fun addMenuItem(itemId: String, name: String, price: Double, inventory: Int) {
        menu[itemId] = Triple(name, price, inventory)
    }

    fun removeMenuItem(itemId: String) {
        menu.remove(itemId)
    }

    fun getMenu(): Map<String, Triple<String, Double, Int>> {
        return menu
    }

    // User Operations
    fun addUser(userId: String, balance: Double) {
        userBalances[userId] = balance
    }

    // Order Operations
    fun createOrder(userId: String, itemIds: List<String>, discountCode: String?): String {
        if (!userBalances.containsKey(userId)) throw RuntimeException("User not found.")
        if (itemIds.isEmpty()) throw RuntimeException("Order must have at least one item.")

        var total = 0.0
        val itemsWithInsufficientInventory = mutableListOf<String>()

        for (itemId in itemIds) {
            val item = menu[itemId] ?: throw RuntimeException("Menu item $itemId not found.")
            if (item.third <= 0) {
                itemsWithInsufficientInventory.add(itemId)
            }
            total += item.second
        }

        if (itemsWithInsufficientInventory.isNotEmpty()) {
            throw RuntimeException("Insufficient inventory for items: $itemsWithInsufficientInventory")
        }

        // Apply discount
        val discount = calculateDiscount(total, discountCode)
        total -= discount

        // Check user balance
        if (userBalances[userId]!! < total) throw RuntimeException("Insufficient balance.")

        // Deplete inventory
        itemIds.forEach { itemId ->
                val item = menu[itemId]!!
                menu[itemId] = Triple(item.first, item.second, item.third - 1)
        }

        // Assign a rider
        if (riders.isEmpty()) throw RuntimeException("No riders available.")
        val assignedRider = riders.removeAt(0)

        // Create order
        val orderId = UUID.randomUUID().toString()
        orders[orderId] = mutableMapOf(
                "userId" to userId,
                "itemIds" to itemIds,
                "total" to total,
                "status" to "Pending",
                "rider" to assignedRider
        )

        // Notify customer and restaurant
        sendNotification(userId, "Your order $orderId has been placed successfully.")
        sendNotification("restaurant", "A new order $orderId has been received.")

        return orderId
    }

    private fun calculateDiscount(total: Double, discountCode: String?): Double {
        return when (discountCode) {
            "DISCOUNT10" -> total * 0.10
            "DISCOUNT20" -> total * 0.20
            else -> 0.0
        }
    }

    // Notification (Deliberately not abstracted to emphasize SRP violation)
    private fun sendNotification(recipient: String, message: String) {
        println("Notification sent to $recipient: $message")
    }

    // Delivery Operations
    fun getDeliveryStatus(orderId: String): String {
        val order = orders[orderId] ?: throw RuntimeException("Order $orderId not found.")
        return order["status"] as String
    }

    fun updateDeliveryStatus(orderId: String, status: String) {
        val order = orders[orderId] ?: throw RuntimeException("Order $orderId not found.")
        order["status"] = status
    }

    // Rider Operations
    fun addRider(riderId: String) {
        riders.add(riderId)
    }

    fun getRiders(): List<String> {
        return riders
    }
}
