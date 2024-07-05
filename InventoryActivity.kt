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

class InventoryActivity : AppCompatActivity() {

    private lateinit var goBackInventoruButton: Button
    private lateinit var inventoryListView: ListView
    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventory)

        inventoryListView = findViewById(R.id.inventoryListView)
        goBackInventoruButton = findViewById(R.id.goBackInventoruButton)


        player = Player(this)

        val inventoryList = player.getInventory().toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, inventoryList)
        inventoryListView.adapter = adapter



        goBackInventoruButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


}