package com.example.nikitabobkov.customviewexample

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.nikitabobkov.customviewexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnHoneycombClickListener {
    private lateinit var binding: ActivityMainBinding
    private var honeycombVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.honeycomb.visibility = if (honeycombVisible) View.VISIBLE else View.INVISIBLE
        binding.honeycomb.setOnClickListener(this)

        binding.honeycombLayout.visibility = if (!honeycombVisible) View.VISIBLE else View.INVISIBLE
        binding.honeycombLayout.setOnClickListener(this)

        binding.honeycomb.visibility = View.INVISIBLE
        binding.honeycombLayout.visibility = View.INVISIBLE
        binding.honeycombTextView.setText("sdfsdfsdfsdfsdfsdfsdfsdfsd")
    }

    override fun onHoneycombClick(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
