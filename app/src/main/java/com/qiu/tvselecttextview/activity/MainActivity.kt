package com.qiu.tvselecttextview.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.qiu.tvselecttextview.R
import com.qiu.tvselecttextview.view.TVSelectTextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvText = findViewById<TVSelectTextView>(R.id.tv_text)
        tvText.requestFocus()
        tvText.setOnTextSelectListener{
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
        }
    }
}