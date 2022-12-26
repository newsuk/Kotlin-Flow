package com.example.kotlinflowexample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {

    lateinit var flowOne: Flow<String>
    lateinit var flowTwo: Flow<String>
    private var button: Button? = null
    private val TAG = SecondActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        button = findViewById<Button>(R.id.button)
        setupFlow()
        setupClicks()
    }

    private fun setupClicks() {
        button?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                flowOne.zip(flowTwo)
                { firstString, secondString ->
                    "$firstString $secondString"
                }.collect {
                    Log.d(TAG, it)
                    delay(500)
                    val textView = findViewById<TextView>(R.id.textView)
                    textView.text = it
                } //Note: Consider if both flows doesn't have the same number of item, then the flow will stop as soon as one of the flow completes.
            }
        }
    }

    private fun setupFlow() {
        flowOne = flowOf("Himanshu", "Amit", "Janishar").flowOn(Dispatchers.Default)
        flowTwo = flowOf("Singh", "Shekhar", "Ali").flowOn(Dispatchers.Default)
    }

}
