package com.example.testgame

import android.os.Handler
import android.content.Context
import android.content.SharedPreferences
import java.util.*
import android.app.Application


class CandyCounter private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private var candyCount: Int = 0

    private val timer = Timer()

    init {
        loadCandyCount()
        startTimer()
    }

    private fun startTimer() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                incrementCandyCount()
            }
        }, 1000, 1000)
    }

    companion object {
        @Volatile
        private var INSTANCE: CandyCounter? = null

        fun getInstance(context: Context): CandyCounter {
            return INSTANCE ?: synchronized(this) {
                val instance = CandyCounter(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getCandyCount(): Int {
        return candyCount
    }

    fun incrementCandyCountFromBattle(amount: Int) {
        candyCount += amount
        saveCandyCount()
    }

    fun incrementCandyCount() {
        candyCount++
        saveCandyCount()
    }

    fun decrementCandyCount() {
        candyCount = 0
        saveCandyCount()
    }

    fun buysinshop(amount: Int) {
        candyCount -= amount
        saveCandyCount()
    }

    fun saveCandyCount() {
        sharedPreferences.edit().putInt("candyCount", candyCount).apply()
    }

    fun loadCandyCount() {
        candyCount = sharedPreferences.getInt("candyCount", 0)
    }
}

