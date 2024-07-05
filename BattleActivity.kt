package com.example.testgame

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random


class BattleActivity : AppCompatActivity() {

    private lateinit var candyCounterTextView: TextView
    private lateinit var playerHealthTextView: TextView
    private lateinit var enemyHealthTextView: TextView
    private lateinit var gobackbattlebutton: Button
    private lateinit var grassBattleButton: Button
    private lateinit var ratbattlebutton: Button

    private lateinit var candyCounter: CandyCounter
    private lateinit var player: Player
    private lateinit var enemy: Enemy


    private val handler = Handler()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        player = Player(this)

        playerHealthTextView = findViewById(R.id.playerHealthTextView)
        enemyHealthTextView = findViewById(R.id.enemyHealthTextView)
        gobackbattlebutton = findViewById(R.id.gobackbattlebutton)
        grassBattleButton = findViewById(R.id.grassbattlebutton)
        ratbattlebutton = findViewById(R.id.ratbattlebutton)

        candyCounterTextView = findViewById(R.id.candyCounterTextView)

        candyCounter = CandyCounter.getInstance(applicationContext)
        enemy = Enemy("", 100, 0, 0, 0)

        gobackbattlebutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        updateHealthTextViews()

        grassBattleButton.setOnClickListener {
            enemy = Enemy("Трава", 1, 0, 0, 10)
            startBattle(enemy)

        }

        ratbattlebutton.setOnClickListener {
            enemy = Enemy("Крыса", 10, 5, 5, 20)
            startBattle(enemy)

        }

        handler.post(object : Runnable {
            override fun run() {
                updateCandyCounter()

                handler.postDelayed(this, 1000) // Обновляем каждую секунду
            }
        })

    }
    // ...........................................................

    private fun updateCandyCounter() {
        candyCounterTextView.text = "Конфеты: ${candyCounter.getCandyCount()}"
    }

    private fun startBattle(enemy: Enemy) {
        if (enemy.getArmor() >= player.getDamage() ){
            showToast("Вы не можете пробить противника. Купите новое оружие")
            return
        }
        while (player.getHealth() > 0 && enemy.getHealth() > 0) {

            playerAttack()
            if (enemy.getHealth() <= 0) {
                // Враг побежден
                victoryReward(enemy)
                break
            }
            enemyAttack()
            if (player.getHealth() <= 0) {
                // Игрок побежден
                defeat()
                break
            }
            updateUI()
        }
    }
    private fun defeat() {
        // Поражение игрока
        showToast("Вы проиграли! В следующий раз будет лучше!")
    }

    private fun updateUI() {
        playerHealthTextView.text = "Здоровье игрока: ${player.getHealth()}"
        enemyHealthTextView.text = "Здоровье врага: ${enemy.getHealth()}"
    }


    private fun victoryReward(enemy: Enemy) {
        // Получаем награду за победу над противником
        val reward = enemy.getReward()
        candyCounter.incrementCandyCountFromBattle(reward)
        showToast("Вы победили! Получено $reward конфет")
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun playerAttack() {
        // Игрок атакует врага
        val damage = player.getDamage()
        val hitChance = Random.nextInt(1, 101) // Шанс попадания от 1 до 100

        if (hitChance <= 80) { // 90% шанс попадания
            enemy.decreaseHealth(damage)
            updateHealthTextViews()
        } else {
            showToast("Вы промахнулись")
        }
    }

    private fun enemyAttack() {
        // Враг атакует игрока
        val damage = enemy.getDamage()
        val hitChance = Random.nextInt(1, 101) // Шанс попадания от 1 до 100

        if (hitChance <= 80) { // 90% шанс попадания
            player.decreaseHealth(damage)
            updateHealthTextViews()
        } else {
            // Промах
        }
    }

    private fun updateHealthTextViews() {
        playerHealthTextView.text = "Здоровье игрока: ${player.getHealth()}"
        enemyHealthTextView.text = "Здоровье врага: ${enemy.getHealth()}"
    }
    override fun onPause() {
        super.onPause()
        // Сохранение количества конфет перед уходом из активити
        candyCounter.saveCandyCount()

    }

}

class Enemy(private val name: String, private var health: Int, private val damage: Int, private val armor: Int, private val reward: Int) {

    fun getHealth(): Int {
        return health
    }
    fun getArmor(): Int {
        return armor
    }

    fun decreaseHealth(amount: Int) {
        health -= amount
    }

    fun getDamage(): Int {
        return damage
    }

    fun getReward(): Int {
        return reward
    }
}

