package com.example.fooddelivery

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FoodDeliverySystemTests {

    private val system = FoodDeliverySystem()

    @Test
    fun `add menu item and retrieve it`() {
        system.addMenuItem("1", "Burger", 5.99, 10)
        val menu = system.getMenu()

        assertEquals(1, menu.size)
        assertTrue(menu.containsKey("1"))
        assertEquals("Burger", menu["1"]?.first)
        assertEquals(5.99, menu["1"]?.second)
        assertEquals(10, menu["1"]?.third) // Check inventory
    }

    @Test
    fun `remove menu item`() {
        system.addMenuItem("1", "Burger", 5.99, 10)
        system.removeMenuItem("1")
        val menu = system.getMenu()

        assertFalse(menu.containsKey("1"))
    }

    @Test
    fun `add user and check balance`() {
        system.addUser("user1", 50.0)
        val balance = system.userBalances["user1"]

        assertNotNull(balance)
        assertEquals(50.0, balance)
    }

    @Test
    fun `create order successfully with sufficient balance and inventory`() {
        system.addMenuItem("1", "Burger", 5.99, 10)
        system.addMenuItem("2", "Pizza", 8.99, 5)
        system.addUser("user1", 50.0)

        val orderId = system.createOrder("user1", listOf("1", "2"), null)
        val order = system.orders[orderId]

        assertNotNull(order)
        assertEquals("Pending", order?.get("status"))
        assertEquals(14.98, order?.get("total"))
        assertEquals(listOf("1", "2"), order?.get("itemIds"))
        assertEquals(9, system.getMenu()["1"]?.third) // Check inventory for Burger
        assertEquals(4, system.getMenu()["2"]?.third) // Check inventory for Pizza
    }

    @Test
    fun `fail to create order due to insufficient inventory`() {
        system.addMenuItem("1", "Burger", 5.99, 0) // No inventory
        system.addUser("user1", 50.0)

        val exception = assertThrows<RuntimeException> {
            system.createOrder("user1", listOf("1"), null)
        }

        assertEquals("Insufficient inventory for items: [1]", exception.message)
    }

    @Test
    fun `fail to create order due to insufficient balance`() {
        system.addMenuItem("1", "Burger", 10.0, 5)
        system.addUser("user1", 5.0) // Insufficient balance

        val exception = assertThrows<RuntimeException> {
            system.createOrder("user1", listOf("1"), null)
        }

        assertEquals("Insufficient balance.", exception.message)
    }

    @Test
    fun `apply discount while creating order`() {
        system.addMenuItem("1", "Burger", 10.0, 5)
        system.addUser("user1", 50.0)

        val orderId = system.createOrder("user1", listOf("1"), "DISCOUNT10")
        val order = system.orders[orderId]

        assertNotNull(order)
        assertEquals(9.0, order?.get("total")) // Total after 10% discount
    }

    @Test
    fun `send notifications after placing an order`() {
        system.addMenuItem("1", "Burger", 5.99, 10)
        system.addUser("user1", 50.0)

        val orderId = system.createOrder("user1", listOf("1"), null)

        // Assume notifications are printed to the console
        // (You can mock the notification system if desired in the refactored version)
        println("Notification should be sent for order $orderId.")
    }

    @Test
    fun `assign rider to order and check delivery status`() {
        system.addMenuItem("1", "Burger", 5.99, 10)
        system.addUser("user1", 50.0)
        system.addRider("rider1")

        val orderId = system.createOrder("user1", listOf("1"), null)
        val order = system.orders[orderId]

        assertNotNull(order)
        assertEquals("rider1", order?.get("rider"))
        assertEquals("Pending", order?.get("status"))
    }

    @Test
    fun `update delivery status of order`() {
        system.addMenuItem("1", "Burger", 5.99, 10)
        system.addUser("user1", 50.0)
        system.addRider("rider1")

        val orderId = system.createOrder("user1", listOf("1"), null)
        system.updateDeliveryStatus(orderId, "Out for Delivery")

        val order = system.orders[orderId]
        assertEquals("Out for Delivery", order?.get("status"))
    }
}