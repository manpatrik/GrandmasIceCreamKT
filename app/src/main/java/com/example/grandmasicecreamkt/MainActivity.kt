package com.example.grandmasicecreamkt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.grandmasicecreamkt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }
    }
}