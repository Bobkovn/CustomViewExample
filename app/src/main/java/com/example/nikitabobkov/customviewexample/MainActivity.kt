package com.example.nikitabobkov.customviewexample

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.nikitabobkov.customviewexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnHoneycombClickListener {
    private lateinit var binding : ActivityMainBinding
    private var counter: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.honeycomb.setOnClickListener(this)
    }

    override fun onHoneycombClick(button: HoneycombButton) {
        button.setText(counter.toString())
        counter += 100
    }
}
