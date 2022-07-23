package com.example.appmegasena

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.edit_number)
        val txtResult: TextView = findViewById(R.id.txt_result)
        val btnGenerate: Button = findViewById(R.id.btn_generate)

        // get the last numbers generated
        prefs = getSharedPreferences("db", Context.MODE_PRIVATE)

        val result = prefs.getString("result", null)
        result?.let { txtResult.text = "Última aposta: $it" }

        btnGenerate.setOnClickListener {
            val text = editText.text.toString()

            numberGenerator(text, txtResult)
        }
    }

    private fun numberGenerator(text: String, txtResult: TextView) {
        // validate input text
        if (text.isEmpty() || (text.toInt() !in 6..15)) {
            Toast.makeText(this, "Informe um número entre 6 e 15!", Toast.LENGTH_LONG).show()
            return
        }

        val numbers = mutableSetOf<Int>()
        val random = Random()
        val qtd = text.toInt()

        while (numbers.size < qtd) {
            val number = random.nextInt(60)
            numbers.add(number + 1)
        }

        // fill result text and sort the numbers
        txtResult.text = numbers.sorted().joinToString(" - ")

        // storage the result in the local device
        prefs.edit().apply {
            putString("result", txtResult.text.toString())
            apply()
        }
    }
}