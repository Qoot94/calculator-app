package com.example.calculator_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var isTrueNumber = true //not decimal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //functions
    fun numberPressed(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (isTrueNumber)
                    tvResult.append(view.text)

                isTrueNumber = false
            } else
                tvResult.append(view.text)
            canAddOperation = true
        }
    }

    fun operatorPressed(view: View) {
        if (view is Button && canAddOperation) {
            tvResult.append(view.text)
            canAddOperation = false
            isTrueNumber = true
        }else if (view is Button && !canAddOperation){
            tvResult.text="can noy"
        }
    }

    fun clearAll(view: View) {
        tvResult.text = ""
       // tvResult.text.isEmpty()

    }

    fun spaceAction(view: View) {
        val length = tvResult.length()
        if (length > 0) tvResult.text = tvResult.text.subSequence(0, length - 1)
    }

    fun showResult(view: View) {
        tvResult.text = calculate()
    }

    private fun calculate(): String {
        val digitsInstance = digitsOperator()
        val dividedBy = timesDiv(digitsInstance)
        val result = addSubFunction(dividedBy)
        if (digitsInstance.isEmpty()) return ""
        if (dividedBy.isEmpty()) return ""

        return result.toString()
    }

    private fun addSubFunction(list: MutableList<Any>): Float {
        var result = list[0] as Float
        for (i in list.indices) {
            if (list[i] is Char && i != list.lastIndex) {
                val operation = list[i]
                val next = list[i + 1] as Float
                when (operation) {
                    '+' -> {
                        result += next
                    }
                    '-' -> {
                        result -= next
                    }
                }

            }
        }
        return result
    }

    private fun timesDiv(digitsInstance: MutableList<Any>): MutableList<Any> {
        var group = digitsInstance
        while (group.contains("*") || group.contains("/")) {
            group = calculateTimesDiv(group)
        }
        return group
    }

    private fun calculateTimesDiv(group: MutableList<Any>): MutableList<Any> {
        val newGroup = mutableListOf<Any>()
        var startIndex = group.size
        for (i in group.indices) {
            if (group[i] is Char && i != group.lastIndex && i < startIndex) {
                val operation = group[i]
                val prev = group[i - 1] as Float
                val next = group[i + 1] as Float
                when (operation) {
                    '*' -> {
                        newGroup.add(prev * next)
                        startIndex = i + 1
                    }
                    '/' -> {
                        if (next.toString() == "0.0"){
                            Log.d("main_rere", next.toString())
                            Log.d("main_rere", prev.toString())
                            Toast.makeText(this,"can't divide by zero",LENGTH_LONG).show()
                        }
                        newGroup.add(prev / next)
                        startIndex = i + 1
                    }
                    else -> {
                        newGroup.add(prev)
                        newGroup.add(operation)
                    }
                }
            }
            if (i > startIndex) newGroup.add(group[i])
        }

        return newGroup
    }

    private fun digitsOperator(): MutableList<Any> {
        val group = mutableListOf<Any>()
        var myDigit = ""
        for (item in tvResult.text) {
            if (item.isDigit() || item == '.') myDigit += item
            else {
                group.add(myDigit.toFloat())
                myDigit = ""
                group.add(item)
            }
        }
        if (!(myDigit == "")) group.add(myDigit.toFloat())
        return group
    }
}