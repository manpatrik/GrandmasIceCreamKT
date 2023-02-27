package com.example.grandmasicecreamkt

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.grandmasicecreamkt.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity(){

    lateinit var binding: ActivityCartBinding;

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ActivityCartBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }
    }
}