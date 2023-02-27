package com.example.grandmasicecreamkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.grandmasicecreamkt.databinding.ActivityIceCreamsBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

class IceCreamsActivity : AppCompatActivity() {

    lateinit var binding: ActivityIceCreamsBinding;
    private val presenter: IceCreamsPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        ActivityIceCreamsBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }

        setSupportActionBar(binding.toolbar)
        presenter.proba()

//        val iceCreamItemAdapter = IceCreamItemAdapter(this, )
//        binding.iceCreamsRecyclerView.adapter

//        val thread = Thread{
//            print(JSONLoader.loadIceCreamsFromJson(this))
//        }
//        thread.start()

    }
}