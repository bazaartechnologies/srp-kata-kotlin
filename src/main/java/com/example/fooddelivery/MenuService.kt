package com.example.fooddelivery

class MenuService {
    private val menu = mutableMapOf<String, Triple<String, Double, Int>>() // itemId -> (name, price, inventory)


    fun validateMenuItem(itemId:String): Triple<String, Double, Int>{
        val itemsWithInsufficientInventory = mutableListOf<String>()
        val item= menu[itemId] ?: throw RuntimeException("Menu item $itemId not found.")
        if (item.third <= 0) {
            itemsWithInsufficientInventory.add(itemId)
        }
        return item
    }


    fun addMenuItem(itemId: String, name: String, price: Double, inventory: Int) {
        menu[itemId] = Triple(name, price, inventory)
    }

    fun getMenuItem(itemId: String):Triple<String, Double, Int>{
        return menu[itemId]!!
    }
    fun depleteMenuItem(itemId:String){
        val item=getMenuItem(itemId)
        menu[itemId] = Triple(item.first, item.second, item.third - 1)
    }

}