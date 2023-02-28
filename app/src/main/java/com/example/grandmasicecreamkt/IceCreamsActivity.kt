package com.example.grandmasicecreamkt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grandmasicecreamkt.databinding.ActivityIceCreamsBinding
import org.koin.android.ext.android.inject

class IceCreamsActivity : AppCompatActivity() {

    lateinit var binding: ActivityIceCreamsBinding;
    private val presenter: IceCreamsPresenterInterface by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityIceCreamsBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)

            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        val iceCreamItemAdapter = IceCreamItemAdapter(this, presenter.getIceCreams(), presenter)
        binding.iceCreamsRecyclerView.adapter = iceCreamItemAdapter
        binding.iceCreamsRecyclerView.setLayoutManager(LinearLayoutManager(this))
        iceCreamItemAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cartMenu) {
            val cartIntent = Intent(this, CartActivity::class.java)
            startActivity(cartIntent)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val cartMenuItem = menu.findItem(R.id.cartMenu)
        val cartActionView = cartMenuItem.actionView
        val cartPcsText = cartActionView!!.findViewById<TextView>(R.id.cartPcsText)
        cartActionView.setOnClickListener { view: View? ->
            onOptionsItemSelected(
                cartMenuItem
            )
        }
        return true
    }
}