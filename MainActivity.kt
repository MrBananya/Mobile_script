package com.example.testgame

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.os.Handler
import android.view.View
import android.content.SharedPreferences
import android.content.Context


class MainActivity : AppCompatActivity() {

    private lateinit var candyCounterTextView: TextView
    private lateinit var healthTextView: TextView
    private lateinit var damageTextView: TextView
    private lateinit var armorTextView: TextView
    private lateinit var eatCandyButton: Button
    private lateinit var goToVillageButton: Button
    private lateinit var goToBattleButton: Button
    private lateinit var goToInventoryButton: Button

    private lateinit var candyCounter: CandyCounter
    private lateinit var player: Player

    private var villageButtonVisible = false
    private var battleButtonVisible = false
    private var inventoryButtonVisible = false

    private lateinit var sharedPreferences: SharedPreferences

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("ButtonVisibility", Context.MODE_PRIVATE)

        candyCounter = CandyCounter.getInstance(applicationContext)



        player = Player(this)


        candyCounterTextView = findViewById(R.id.candyCounterTextView)
        healthTextView = findViewById(R.id.healthTextView)
        damageTextView = findViewById(R.id.damageTextView)
        armorTextView = findViewById(R.id.armorTextView)
        eatCandyButton = findViewById(R.id.eatCandyButton)
        goToVillageButton = findViewById(R.id.goToVillageButton)
        goToBattleButton = findViewById(R.id.goToBattleButton)
        goToInventoryButton = findViewById(R.id.goToInventoryButton)

        updateUI()
        updateCandyCounter()


        eatCandyButton.setOnClickListener {
            if (candyCounter.getCandyCount() >= 10) {
                eatCandies()
                updateUI()
            }
        }

        goToVillageButton.setOnClickListener {
            val intent = Intent(this, VillageActivity::class.java)
            startActivity(intent)
        }

        goToBattleButton.setOnClickListener {
            val intent = Intent(this, BattleActivity::class.java)
            startActivity(intent)
        }

        goToInventoryButton.setOnClickListener {
            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)
        }


        handler.post(object : Runnable {
            override fun run() {
                updateCandyCounter()
                updateUI()
                handler.postDelayed(this, 1000) // Обновляем каждую секунду
            }
        })

    }
    //.......................................................... override fun onCreate


    private fun eatCandies() {
        val beforeatenCandies = candyCounter.getCandyCount()
        val eatenCandies = candyCounter.getCandyCount() / 50
        if (eatenCandies > 0) {
            player.increaseHealth(eatenCandies)
            candyCounter.decrementCandyCount()
        }
        updateCandyCounter()
    }

    private fun updateCandyCounter() {
        candyCounterTextView.text = "Конфеты: ${candyCounter.getCandyCount()}"
    }

    private fun updateUI() {
        candyCounterTextView.text = "Конфеты: ${candyCounter.getCandyCount()}"
        healthTextView.text = "Здоровье: ${player.getHealth()}"
        damageTextView.text = "Урон: ${player.getDamage()}"
        armorTextView.text = "Броня: ${player.getArmor()}"

        if (candyCounter.getCandyCount()  >= 10 && villageButtonVisible == false) {
            goToVillageButton.visibility = View.VISIBLE
            villageButtonVisible = true
        }
        else if (villageButtonVisible == true)
        {
            goToVillageButton.visibility = View.VISIBLE
        }



        if (candyCounter.getCandyCount() >= 35 && (!battleButtonVisible || !inventoryButtonVisible))
        {
            goToBattleButton.visibility = View.VISIBLE
            goToInventoryButton.visibility = View.VISIBLE
            inventoryButtonVisible = true
            battleButtonVisible = true
        }
        else if (battleButtonVisible || inventoryButtonVisible)
        {
            goToBattleButton.visibility = View.VISIBLE
            goToInventoryButton.visibility = View.VISIBLE
        }



    }

    override fun onPause() {
        super.onPause()
        // Сохранение количества конфет перед уходом из активити
        candyCounter.saveCandyCount()
        val editor = sharedPreferences.edit()
        editor.putBoolean("villageButtonVisible", villageButtonVisible)
        editor.putBoolean("battleButtonVisible", battleButtonVisible)
        editor.putBoolean("inventoryButtonVisible", inventoryButtonVisible)
        editor.apply()

    }

    override fun onResume() {
        super.onResume()
        villageButtonVisible = sharedPreferences.getBoolean("villageButtonVisible", false)
        battleButtonVisible = sharedPreferences.getBoolean("battleButtonVisible", false)
        inventoryButtonVisible = sharedPreferences.getBoolean("inventoryButtonVisible", false)

        // Примените видимость кнопок в соответствии с их сохраненным состоянием
        if (villageButtonVisible) {
            goToVillageButton.visibility = View.VISIBLE
        } else {
            goToVillageButton.visibility = View.INVISIBLE
        }

        if (battleButtonVisible || inventoryButtonVisible) {
            goToBattleButton.visibility = View.VISIBLE
            goToInventoryButton.visibility = View.VISIBLE
        } else {
            goToBattleButton.visibility = View.INVISIBLE
            goToInventoryButton.visibility = View.INVISIBLE
        }
    }


}

