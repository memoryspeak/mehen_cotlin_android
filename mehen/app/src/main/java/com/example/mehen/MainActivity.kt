package com.example.mehen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    var mehenModel = MehenModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "$mehenModel")
    }
}