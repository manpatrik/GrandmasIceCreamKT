package com.example.grandmasicecreamkt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grandmasicecreamkt.databinding.ActivityIceCreamsBinding
import org.koin.android.ext.android.inject

class IceCreamsActivity : AppCompatActivity() {

    lateinit var binding: ActivityIceCreamsBinding;
    private val presenter: IceCreamsPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        ActivityIceCreamsBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }

//        val iceCreams = ArrayList<IceCream>()
//        val iceCream = IceCream(1,"vanilia", IceCream.Status.AVAILABLE, "")
//        iceCreams.add(iceCream)
//        iceCreams.add(iceCream)
//        iceCreams.add(iceCream)
//
        val iceCreamItemAdapter = IceCreamItemAdapter(this, presenter.getIceCreams())
        binding.iceCreamsRecyclerView.adapter = iceCreamItemAdapter
        binding.iceCreamsRecyclerView.setLayoutManager(LinearLayoutManager(this))
        iceCreamItemAdapter.notifyDataSetChanged()

    }
}