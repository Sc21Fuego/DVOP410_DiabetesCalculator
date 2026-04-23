@file:Suppress("SpellCheckingInspection")

package com.example.diabetescalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged

class MainActivity : AppCompatActivity() {
    lateinit var txtOutput: TextView
    lateinit var edtEAG: EditText
    lateinit var edtA1C: EditText
    @Suppress("SpellCheckingInspection")
    lateinit var radADAG: RadioButton
    lateinit var radDCCT: RadioButton
    lateinit var btnCalcA1C: Button
    lateinit var btnCalcEAG: Button
    var calculationConst: Double = 46.7
    var calculationMult: Double = 28.7
    var calculationMethod: String = "ADAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initApplication()
    }

    private fun initApplication() {
        txtOutput = findViewById(R.id.txtOutput)
        edtEAG = findViewById(R.id.edtEAG)
        edtA1C = findViewById(R.id.edtA1C)
        radADAG = findViewById(R.id.radADAG)
        radDCCT = findViewById(R.id.radDCCT)
        btnCalcA1C = findViewById(R.id.btnCalcA1C)
        btnCalcEAG = findViewById(R.id.btnCalcEAG)

//  Clear opposing field when text is entered into either field
//        Note: With this, there should only be 1 button, but leaving both buttons to fit assignment parameters.
        edtEAG.doAfterTextChanged { text ->
            // Only clear opposing field & update button states after user starts typing
            if (edtEAG.hasFocus()) {
                edtA1C.text.clear()
                btnCalcEAG.isEnabled = false
                btnCalcEAG.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.disabled)
                btnCalcA1C.isEnabled = true
                btnCalcA1C.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.primaryRed)
            }
        }

        edtA1C.doAfterTextChanged { text ->
            if (edtA1C.hasFocus()) {
                edtEAG.text.clear()
                btnCalcA1C.isEnabled = false
                btnCalcA1C.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.disabled)
                btnCalcEAG.isEnabled = true
                btnCalcEAG.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.primaryRed)
            }
        }
    }

    //    Radial buttons toggle the two static inputs for the calculations and update method called for output string
    fun radBtnFormulaToggleOnClick(v: View) {
        //    ADAG Formula: eAG = (28.7 * A1C) – 46.7
        //    DCCT Formula: eAG = (A1c * 35.6) - 77.3
        if (radADAG.isChecked) {
            calculationConst = 46.7
            calculationMult = 28.7
            calculationMethod = "ADAG"
        } else {
            calculationConst = 77.3
            calculationMult = 35.6
            calculationMethod = "DCCT"
        }
    }

//    Calculation functions for both inverse cases. Relies on radial button state for correct constants.
//    Returns string to be used in output function
    fun calcA1C(inputEAG: Double): String {
        val equation = (inputEAG + calculationConst) / calculationMult
        return String.format("%.1f", equation)
    }

    fun calcEAG(inputA1C: Double): String {
        val equation = (inputA1C * calculationMult) - calculationConst
        return String.format("%.0f", equation)
    }

    // Reads eAG value to calculate A1c and sends result to Output field.
    // Throws a toast if eAG is blank when invoked
    fun btnCalcA1cOnPress(v: View) {
        val inputEAG = edtEAG.text.toString().toDoubleOrNull()

        if (inputEAG != null) {
            val outputText = "A1c: ${calcA1C(inputEAG)}\n(Calculated using $calculationMethod)"
            txtOutput.text = outputText
        } else {
            Toast.makeText(
                applicationContext,
                "Please enter a number for EAG and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Reads A1c value to calculate eAG and sends result to Output field.
    // Throws a toast if A1c is blank when invoked
    fun btnCalcEAGOnPress(v: View) {
        val inputA1C = edtA1C.text.toString().toDoubleOrNull()

        if (inputA1C != null) {
            val outputText = "EAG: ${calcEAG(inputA1C)}\n(Calculated using $calculationMethod)"
            txtOutput.text = outputText
        } else {
            Toast.makeText(
                applicationContext,
                "Please enter a number for A1C and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}