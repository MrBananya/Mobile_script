package com.example.testgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ListView
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.TextView
import android.content.SharedPreferences
import android.content.Context
import android.os.Handler

class VillageActivity : AppCompatActivity() {

    private lateinit var buySwordButton: Button
    private lateinit var buyArmorButton: Button

    private var swordButtonVisible = true
    private var armorButtonVisible = true

    private lateinit var candyCounterTextView: TextView
    private lateinit var emptysmithtextTextView: TextView
    private lateinit var goBackVillageButton: Button
    private lateinit var blacksmithButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var player: Player
    private lateinit var candyCounter: CandyCounter

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_village)

        sharedPreferences = getSharedPreferences("ButtonVisibility", Context.MODE_PRIVATE)


        candyCounter = CandyCounter.getInstance(applicationContext)

        player = Player(this)



        candyCounterTextView = findViewById(R.id.candyCounterTextView)
        goBackVillageButton = findViewById(R.id.goBackVillageButton)
        blacksmithButton = findViewById(R.id.blacksmithButton)
        emptysmithtextTextView = findViewById(R.id.emptysmithtext)

        buySwordButton = findViewById(R.id.buySwordButton)
        buyArmorButton = findViewById(R.id.buyArmorButton)


        blacksmithButton.setOnClickListener {
            showBlacksmithShop()
        }
        buySwordButton.setOnClickListener {
            buyEquipment("Меч", 100)
        }
        buyArmorButton.setOnClickListener {
            buyEquipment("Броня", 200)
        }


        goBackVillageButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        updateCandyCounter()

        // ..............................................Для правильного отображения ?
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler.post(object : Runnable {
            override fun run() {
                updateCandyCounter()
                handler.postDelayed(this, 1000) // Обновляем каждую секунду
            }
        })

    }
    // ....................................................После override fun onCreate
    private fun showBlacksmithShop() {
        if (swordButtonVisible){
            buySwordButton.visibility = View.VISIBLE
        } else {
            buySwordButton.visibility = View.INVISIBLE
        }

        if (armorButtonVisible){
            buyArmorButton.visibility = View.VISIBLE
        } else {
            buyArmorButton.visibility = View.INVISIBLE
        }
        if (swordButtonVisible == false && armorButtonVisible == false){
            emptysmithtextTextView.visibility = View.VISIBLE
            emptysmithtextTextView.text = "Ты купил все что было"
        }

    }

    private fun buyEquipment(itemName: String, price: Int) {
        if (candyCounter.getCandyCount() >= price) {
            candyCounter.buysinshop(price)
            showToast("Предмет куплен: $itemName")

            // Добавляем предмет в инвентарь и сохраняем его
            player.addItemToInventory(itemName)
            player.saveInventory()

            // Делаем кнопку невидимой, чтобы нельзя было повторно купить предмет
            if (itemName == "Меч") {
                buySwordButton.visibility = View.INVISIBLE
                swordButtonVisible = false
            } else if (itemName == "Броня") {
                buyArmorButton.visibility = View.INVISIBLE
                armorButtonVisible = false
            }
        } else {
            showToast("Недостаточно конфет для покупки $itemName")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun updateCandyCounter() {
        val candyCount = candyCounter.getCandyCount()
        candyCounterTextView.text = "Конфеты: $candyCount"
    }

    override fun onPause() {
        super.onPause()
        candyCounter.saveCandyCount()
        val editor = sharedPreferences.edit()
        editor.putBoolean("swordButtonVisible", swordButtonVisible)
        editor.putBoolean("armorButtonVisible", armorButtonVisible)
        editor.apply()


    }

    override fun onResume() {
        super.onResume()
        swordButtonVisible = sharedPreferences.getBoolean("swordButtonVisible", true)
        armorButtonVisible = sharedPreferences.getBoolean("armorButtonVisible", true)

    }


}