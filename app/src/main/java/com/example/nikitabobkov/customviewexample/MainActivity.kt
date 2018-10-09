package com.example.nikitabobkov.customviewexample

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.nikitabobkov.customviewexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnHoneycombClickListener {
    private lateinit var binding: ActivityMainBinding
    private var counter: Int = 0
    private var honeycombVisible: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.honeycomb.visibility = if (honeycombVisible) View.VISIBLE else View.INVISIBLE
        binding.honeycombLayout.visibility = if (!honeycombVisible) View.VISIBLE else View.INVISIBLE
        binding.honeycomb.setOnClickListener(this)
        binding.honeycombLayout.setHoneycombAmount(10)
    }

    override fun onHoneycombClick() {
        counter += 50
        //binding.honeycomb.setRadius(counter.toFloat())
        binding.honeycomb.setText(counter.toString())
    }
}
