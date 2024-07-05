package com.example.testgame

import android.content.Context
import android.content.SharedPreferences

class Player(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("PlayerPrefs", Context.MODE_PRIVATE)

    private var inventory = sharedPreferences.getStringSet("inventory", mutableSetOf()) ?: mutableSetOf()

    // Значения по умолчанию для здоровья, урона и брони
    private var health = sharedPreferences.getInt("health", 100)
    private var damage = sharedPreferences.getInt("damage", 1)
    private var armor = sharedPreferences.getInt("armor", 1)

    init {
        loadPlayerData()
    }


    // Функции для получения значений параметров игрока
    fun getHealth(): Int {
        return health
    }

    fun getDamage(): Int {
        return damage
    }

    fun getArmor(): Int {
        return armor
    }

    // Функции для изменения значений параметров игрока
    fun increaseHealth(amount: Int) {
        health += amount
        savePlayerStats()
    }

    fun decreaseHealth(amount: Int) {
        health -= amount
        savePlayerStats()
    }

    fun increaseDamage(amount: Int) {
        damage += amount
        savePlayerStats()
    }

    fun increaseArmor(amount: Int) {
        armor += amount
        savePlayerStats()
    }

    private fun savePlayerStats() {
        // Сохраняем параметры игрока в SharedPreferences
        sharedPreferences.edit()
            .putInt("health", health)
            .putInt("damage", damage)
            .putInt("armor", armor)
            .apply()
    }

    fun addItemToInventory(itemName: String) {
        inventory.add(itemName)
        when (itemName) {
            "Меч" -> {
                // Повышаем урон на 10
                damage += 10
            }
            "Броня" -> {
                // Повышаем броню на 5
                armor += 5
            }
            // Другие предметы добавляются аналогично
        }
        // Сохраняем обновленные данные
        savePlayerData()
    }


    fun removeItemFromInventory(itemName: String) {
        inventory.remove(itemName)
    }

    fun saveInventory() {
        sharedPreferences.edit().putStringSet("inventory", inventory).apply()
    }

    fun getInventory(): Set<String> {
        return inventory
    }

    fun savePlayerData() {
        sharedPreferences.edit().apply {
            putInt("damage", damage)
            putInt("armor", armor)
            putInt("health", health)
            apply()
        }
    }

    private fun loadPlayerData() {
        damage = sharedPreferences.getInt("damage", damage)
        armor = sharedPreferences.getInt("armor", armor)
        health = sharedPreferences.getInt("health", health)
    }


}
