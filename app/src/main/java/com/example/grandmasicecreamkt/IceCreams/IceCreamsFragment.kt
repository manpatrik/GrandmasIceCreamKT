package com.example.grandmasicecreamkt.IceCreams

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.CartF.CartFragment
import com.example.grandmasicecreamkt.databinding.ActivityIceCreamsBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class IceCreamsFragment : Fragment(), IceCreamFragmentInterFace {

    lateinit var binding: ActivityIceCreamsBinding;
    private val presenter: IceCreamsPresenterInterface by inject() { parametersOf(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = ActivityIceCreamsBinding.inflate(inflater, container, false).apply {
        binding = this

        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)?.setTitle("")
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val iceCreamItemAdapter = IceCreamItemAdapter(view.context, presenter.getIceCreams(), presenter)
        binding.iceCreamsRecyclerView.adapter = iceCreamItemAdapter
        binding.iceCreamsRecyclerView.setLayoutManager(LinearLayoutManager(view.context))
        iceCreamItemAdapter.notifyDataSetChanged()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cartMenu) {
            findNavController().navigate(R.id.action_iceCreamsActivity_to_cartFragment)
        }
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val cartMenuItem = menu.findItem(R.id.cartMenu)
        val cartActionView = cartMenuItem.actionView
        val cartPcsText = cartActionView!!.findViewById<TextView>(R.id.cartPcsText)
        cartActionView.setOnClickListener { view: View? ->
            onOptionsItemSelected(
                cartMenuItem
            )
        }
    }
}