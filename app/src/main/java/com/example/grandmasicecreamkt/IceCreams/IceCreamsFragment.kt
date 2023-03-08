package com.example.grandmasicecreamkt.IceCreams

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.databinding.ActivityIceCreamsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class IceCreamsFragment : Fragment() {

    private lateinit var binding: ActivityIceCreamsBinding;
    private val viewModel: IceCreamsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = ActivityIceCreamsBinding.inflate(inflater, container, false).apply {
        binding = this

        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)?.setTitle("")
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.iceCreams.observe(viewLifecycleOwner) {
            if (it is Resource.Success){
                val iceCreamItemAdapter = IceCreamItemAdapter(view.context, it.data, viewModel)
                binding.iceCreamsRecyclerView.adapter = iceCreamItemAdapter
                binding.iceCreamsRecyclerView.setLayoutManager(LinearLayoutManager(view.context))
                iceCreamItemAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cartMenu) {
            findNavController().navigate(IceCreamsFragmentDirections.actionIceCreamsActivityToCartFragment())
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