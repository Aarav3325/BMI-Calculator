package com.aarav.bmicalculator2

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var weightText : EditText
    private lateinit var heightText : EditText
    private lateinit var calcButton : Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weightText = findViewById(R.id.etWeight)
        heightText = findViewById(R.id.etHeight)
        calcButton = findViewById(R.id.btnCalculate)

        sharedPreferences = getSharedPreferences("my_sf", MODE_PRIVATE)
        editor = sharedPreferences.edit()



        calcButton.setOnClickListener {

            val weight = weightText.text.toString()
            val height = heightText.text.toString()


                if (validateInput(weight, height)) {
                    val bmi =
                        (weight.toFloat()) / ((height.toFloat() / 100) * (height.toFloat() / 100))
                    val bmi2D = String.format("%.2f", bmi).toFloat()
                    displayResult(bmi2D)
                }
            }


    }

    override fun onPause() {
        super.onPause()
        val weight = weightText.text.toString().toInt()
        val height = heightText.text.toString().toInt()
        editor.apply{
            putInt("sf_weight", weight)
            putInt("sf_height", height)
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
        val weight = sharedPreferences.getInt("sf_weight", 0)
        val height = sharedPreferences.getInt("sf_height", 0)
        if(weight!=0){
            weightText.setText(weight.toString())
        }
        if(height!=0){
            heightText.setText(height.toString())
        }
    }

    private fun validateInput(weight : String?, height : String?):Boolean{
        if (weight.isNullOrEmpty() && height.isNullOrEmpty()) {
            Toast.makeText(this, "Enter weight and height", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return when {
                weight.isNullOrEmpty() -> {
                    Toast.makeText(this, "Enter weight", Toast.LENGTH_SHORT).show()
                    return false
                }

                height.isNullOrEmpty() -> {
                    Toast.makeText(this, "Enter height", Toast.LENGTH_SHORT).show()
                    return false
                }

                else -> {
                    return true
                }
            }
        }
    }

    private fun displayResult(bmi : Float){
        var resultIndex = findViewById<TextView>(R.id.tvIndex)
        var resultDescription = findViewById<TextView>(R.id.tvResult)
        var info = findViewById<TextView>(R.id.tvInfo)

        resultIndex.text = bmi.toString()
        info.text = "(Normal range is 18.5 - 24.9)"

        var resultText = ""
        var color = 0

        when{
            bmi < 18.50 -> {
                resultText = "Underweight"
                color = R.color.underweight
            }
            bmi in 18.50..24.99 ->{
                resultText = "Healthy"
                color = R.color.normal
            }
            bmi in 25.00..29.99 -> {
                resultText = "Overweight"
                color = R.color.overwieght
            }
            bmi > 29.99 -> {
                resultText = "Obese"
                color = R.color.obese
            }
        }

        resultDescription.setTextColor(ContextCompat.getColor(this, color))
        resultDescription.text = resultText
    }
}