package com.example.kotlinflowexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var button: Button? = null
    lateinit var flow: Flow<Int>
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById<Button>(R.id.button)
        setupFlow()
        setupClicks()
        val buttonNext = findViewById<Button>(R.id.button1)
        buttonNext.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

    /**
     *  is the function where we will define the flow
     */
    private fun setupFlow() {
        flow = flow {
            Log.d(TAG, "Start flow")
            (0..10).forEach {
                // Emit items with 500 milliseconds delay
                delay(500)
                Log.d(TAG, "Emitting $it")
                emit(it) //which collects the value emitted
            }
        }.map {
            it * it //Anything, written above flowOn will run in background thread.
        }.flowOn(Dispatchers.Default) //is like subscribeOn in Rxjava
    }

    /**
     * is the function where we will click the button to display the data which is emitted from the flow.
     */
    private fun setupClicks() {
        button?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                flow.collect {
                    Log.d(TAG, it.toString())
                    val textview = findViewById<TextView>(R.id.textView)
                    textview.text = it.toString()
                }
            }
        }
    }

    private fun buildersFlow() {
        //flowOf() - It is used to create flow from a given set of values
        flowOf(4, 2, 5, 1, 7).onEach { delay(400) }.flowOn(Dispatchers.Default)

        // asFlow() - It is an extension function that helps to convert type into flows
        (1..5).asFlow().onEach { delay(300) }.flowOn(Dispatchers.Default)

    }

}
