package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*       //layout ID

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"


class MainActivity : AppCompatActivity() {
//    private lateinit var result: EditText
//    private lateinit var newNumber: EditText
//    private val displayOperation by lazy(LazyThreadSafetyMode.NONE){findViewById<TextView>(R.id.operation)}

    private var operand1:Double? = null
    private var pendingOperation = "="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)

        //데이터 버튼
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.buttonDot)*/

        //수식 버튼
        val buttonEquals = findViewById<Button>(R.id.buttonEquals)
        val buttonDivide = findViewById<Button>(R.id.buttonDivide)
        val buttonMultiply = findViewById<Button>(R.id.buttonMultiply)
        val buttonMinus = findViewById<Button>(R.id.buttonMinus)
        val buttonPlus= findViewById<Button>(R.id.buttonPlus)

        //숫자를 클릭시 텍스트뷰에 새겨짐
        val listner = View.OnClickListener{ v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listner)
        button1.setOnClickListener(listner)
        button2.setOnClickListener(listner)
        button3.setOnClickListener(listner)
        button4.setOnClickListener(listner)
        button5.setOnClickListener(listner)
        button6.setOnClickListener(listner)
        button7.setOnClickListener(listner)
        button8.setOnClickListener(listner)
        button9.setOnClickListener(listner)
        buttonDot.setOnClickListener(listner)

        //버튼 클릭시 수식 새겨짐
        val opListner = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try{
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            }catch (e: NumberFormatException){      //dot을 누르고 수식을 넣었을때 일어나는 오류 수정
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }

        buttonEquals.setOnClickListener(opListner)
        buttonDivide.setOnClickListener(opListner)
        buttonMultiply.setOnClickListener(opListner)
        buttonMinus.setOnClickListener(opListner)
        buttonPlus.setOnClickListener(opListner)

        buttonNeg.setOnClickListener({view ->
            val value = newNumber.text.toString()
            if (value.isEmpty()){
                newNumber.setText("-")
            }else{
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                }catch (e : java.lang.NumberFormatException){
                    newNumber.setText("")
                }
            }
        })


        buttonClean.setOnClickListener(View.OnClickListener {
            newNumber.setText("")
            result.setText("")
            operation.setText("")
            operand1 = null
            pendingOperation = "="
        }
        )
    }

    //수식 결과값
    private fun performOperation(value:Double, operation:String){
        if(operand1 == null){
            operand1 = value
        }else{
            if (pendingOperation == "="){
                pendingOperation = operation
            }
            when(pendingOperation){
                "=" -> operand1 = value
                "/" -> if (value == 0.0) operand1 = Double.NaN else{
                            operand1 = operand1!! / value
                        }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(operand1 != null){
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)){
            savedInstanceState.getDouble(STATE_OPERAND1)
        }else{
            null
        }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        operation.text = pendingOperation
    }
}
