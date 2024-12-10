import com.example.fooddelivery.FoodDeliverySystem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.*

class FoodDeliverySystemTest {
    private lateinit var system: FoodDeliverySystem

    @BeforeEach
    fun setup() {
        system = FoodDeliverySystem()
    }

    @Test
    fun testAddMenuItem() {
        system.addMenuItem("item1", "Burger", 5.99, 10)
        val menu = system.getMenu()
        assertTrue(menu.containsKey("item1"))
        assertEquals("Burger", menu["item1"]?.first)
    }

    @Test
    fun testRemoveMenuItem() {
        system.addMenuItem("item1", "Burger", 5.99, 10)
        system.removeMenuItem("item1")
        val menu = system.getMenu()
        assertFalse(menu.containsKey("item1"))
    }

    @Test
    fun testAddUser() {
        system.addUser("user1", 20.0)
        // Test is successful if no exceptions are thrown
    }

    @Test
    fun testCreateOrderSuccess() {
        system.addMenuItem("item1", "Burger", 5.99, 10)
        system.addUser("user1", 20.0)
        system.addRider("rider1")

        val orderId = system.createOrder("user1", listOf("item1"), null)
        val deliveryStatus = system.getDeliveryStatus(orderId)

        assertNotNull(orderId)
        assertEquals("Pending", deliveryStatus)
    }

    @Test
    fun testCreateOrderInsufficientBalance() {
        system.addMenuItem("item1", "Burger", 5.99, 10)
        system.addUser("user1", 5.0)
        system.addRider("rider1")

        val exception = assertFailsWith<RuntimeException> {
            system.createOrder("user1", listOf("item1"), null)
        }

        assertEquals("Insufficient balance.", exception.message)
    }

    @Test
    fun testCreateOrderInsufficientInventory() {
        system.addMenuItem("item1", "Burger", 5.99, 0)
        system.addUser("user1", 20.0)
        system.addRider("rider1")

        val exception = assertFailsWith<RuntimeException> {
            system.createOrder("user1", listOf("item1"), null)
        }

        assertTrue(exception.message!!.contains("Insufficient inventory"))
    }

    @Test
    fun testCreateOrderNoRidersAvailable() {
        system.addMenuItem("item1", "Burger", 5.99, 10)
        system.addUser("user1", 20.0)

        val exception = assertFailsWith<RuntimeException> {
            system.createOrder("user1", listOf("item1"), null)
        }

        assertEquals("No riders available.", exception.message)
    }

    @Test
    fun testCreateOrderWithDiscount() {
        system.addMenuItem("item1", "Burger", 10.0, 10)
        system.addUser("user1", 20.0)
        system.addRider("rider1")

        val orderId = system.createOrder("user1", listOf("item1"), "DISCOUNT10")
        val orderDetails = system.getDeliveryStatus(orderId)

        assertNotNull(orderId)
        assertEquals("Pending", orderDetails)
    }

    @Test
    fun testUpdateDeliveryStatus() {
        system.addMenuItem("item1", "Burger", 5.99, 10)
        system.addUser("user1", 20.0)
        system.addRider("rider1")

        val orderId = system.createOrder("user1", listOf("item1"), null)
        system.updateDeliveryStatus(orderId, "Delivered")

        val status = system.getDeliveryStatus(orderId)
        assertEquals("Delivered", status)
    }

    @Test
    fun testAddRider() {
        system.addRider("rider1")
        val riders = system.getRiders()
        assertTrue(riders.contains("rider1"))
    }

    @Test
    fun testRiderRemovedAfterAssignment() {
        system.addMenuItem("item1", "Burger", 5.99, 10)
        system.addUser("user1", 20.0)
        system.addRider("rider1")

        system.createOrder("user1", listOf("item1"), null)
        val riders = system.getRiders()

        assertFalse(riders.contains("rider1"))
    }
}