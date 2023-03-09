package com.example.grandmasicecreamkt.CartF

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.CartF.ui.theme.GrandmasIceCreamKTTheme
import com.example.grandmasicecreamkt.IceCreams.IceCreamsFragmentDirections
import com.example.grandmasicecreamkt.R
import com.example.grandmasicecreamkt.databinding.ActivityCartBinding
import com.example.grandmasicecreamkt.databinding.ActivityIceCreamsBinding
import com.example.grandmasicecreamkt.databinding.CartItemBinding
import com.example.grandmasicecreamkt.databinding.FragmentCartBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment(){

    //private lateinit var adapter: CartItemAdapter
    private val viewModel: CartViewModel by viewModel()

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentCartBinding.inflate(inflater, container, false).apply {
        // binding = this
        this.root.setContent {
            //val cartData = viewModel.cartData.collectAsState()
            //ShowCarts(cartData.value.cartItems, cartData.value.extras)
            ShowCarts(
                listOf(
                    CartItem("1", IceCream(1L, "vanilia", IceCream.Status.AVAILABLE, ""), mutableListOf(5L, 6L), true),
                    CartItem("2", IceCream(2L, "Pisztácia", IceCream.Status.AVAILABLE, ""), mutableListOf(2L, 4L), true),
                    CartItem("3", IceCream(3L, "Csoki", IceCream.Status.AVAILABLE, ""), mutableListOf(2L, 5L, 6L, ), false)
                ), listOf(
                    Extra("Tölcsérek", true, mutableListOf(Item(1L, "Normál t", 2.0), Item(2L, "édes t", 2.0), Item(3L, "csokis t", 2.0))),
                    Extra("Egyébb", false, mutableListOf(Item(4L, "roletti", 2.0), Item(5L, "cukor szórás", 2.0), Item(6L, "ostya", 2.0))),
                )
            )
        }
    }.root

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ShowCarts(cartItems: List<CartItem>, allExtra: List<Extra>) {
        Scaffold(
            topBar =  {
                TopAppBar(
                    backgroundColor = colorResource(id = R.color.red),
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_logo),
                            contentDescription = "Grandma's icecream",
                            Modifier.height(40.dp)
                        )
                    }
                )
            }
        ) {
            LazyColumn(Modifier.padding(start = 10.dp, end = 10.dp)){
                items(cartItems) {cartItem ->
                    Row( Modifier.padding(top = 10.dp) ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_delete), 
                            contentDescription = cartItem.iceCream.name + " törlése a kosárból",
                            Modifier
                                .padding(end = 5.dp, top = 10.dp)
                                .clip(RoundedCornerShape(25))
                                .background(color = colorResource(id = R.color.grey))
                                .clickable { viewModel.removeCartItem(cartItem) }
                        )
                        
                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(color = colorResource(id = R.color.red))
                                .padding(10.dp)
                                .clickable {
                                    viewModel.changeExpandedStatus(cartItem)
                                }
                        ) {
                            Text(text = cartItem.iceCream.name, fontSize = 30.sp, modifier = Modifier.padding(top=5.dp))

                            if (cartItem.expanded){
                                Column(Modifier.fillMaxSize()) {
                                    allExtra.forEach { extra ->
                                        Text(text = extra.type)

                                        val selectedValue = remember { mutableStateOf(-1L) }
                                        val isSelectedItem: (Long) -> Boolean = { selectedValue.value == it }
                                        val onChangeState: (Long) -> Unit = { selectedValue.value = it }


                                        if(extra.required){
                                            cartItem.extraItemIds.forEach { extraItemId ->
                                                extra.items.find { it.id == extraItemId }?.let {
                                                    onChangeState(it.id)
                                                }
                                            }
                                            if( selectedValue.value == -1L) {
                                                onChangeState(extra.items.get(0).id)
                                                // viewModel.addOrRemoveExtraIdfromCart(cartItem, selectedValue.value, true)
                                            }
                                        }

                                        extra.items.forEach { extraItem ->
                                            Row(Modifier.padding(top=5.dp), verticalAlignment = Alignment.CenterVertically) {
                                                if(extra.required){
                                                    RadioButton(
                                                        selected = isSelectedItem(extraItem.id),
                                                        onClick = {
                                                            viewModel.addOrRemoveExtraIdfromCart(cartItem, selectedValue.value, false)
                                                            onChangeState(extraItem.id)
                                                            viewModel.addOrRemoveExtraIdfromCart(cartItem, selectedValue.value, true)
                                                        })
                                                } else {
                                                    val isChecked = remember { mutableStateOf( cartItem.extraItemIds.contains(extraItem.id)) }
                                                    Checkbox(
                                                        checked = isChecked.value,
                                                        onCheckedChange = {
                                                            isChecked.value = it
                                                            viewModel.addOrRemoveExtraIdfromCart(cartItem, extraItem.id, it)
                                                        }
                                                    )
                                                }
                                                Text(text = extraItem.name)
                                            }
                                        }
                                    }
                                }
                            } else { // expanded == false
                                Column(
                                    Modifier
                                        .padding(start = 15.dp)
                                        .fillMaxSize()) {
                                    cartItem.extraItemIds.forEach { extraItemId ->
                                        allExtra.forEach { extra ->
                                            extra.items.find { it.id == extraItemId }?.let {
                                                Text(text = "- " + it.name)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        GrandmasIceCreamKTTheme {
            ShowCarts(
                listOf(
                    CartItem("1", IceCream(1L, "vanilia", IceCream.Status.AVAILABLE, ""), mutableListOf(5L, 6L), true),
                    CartItem("2", IceCream(2L, "Pisztácia", IceCream.Status.AVAILABLE, ""), mutableListOf(2L, 4L), false),
                    CartItem("3", IceCream(3L, "Csoki", IceCream.Status.AVAILABLE, ""), mutableListOf(2L, 5L, 6L, ), false)
                ), listOf(
                    Extra("Tölcsérek", true, mutableListOf(Item(1L, "Normál t", 2.0), Item(2L, "édes t", 2.0), Item(3L, "csokis t", 2.0))),
                    Extra("Egyébb", false, mutableListOf(Item(4L, "roletti", 2.0), Item(5L, "cukor szórás", 2.0), Item(6L, "ostya", 2.0))),
                )
            )
        }
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.cartLayout.apply {
//            this.layoutManager = LinearLayoutManager(this.context)
//        }
//
//        viewModel.cartData.observe(viewLifecycleOwner){data ->
//            binding.cartLayout.adapter = CartItemAdapter(viewModel, data.extras).also {
//                this@CartFragment.adapter = it
//                this@CartFragment.adapter.submitList(data.cartItems.toMutableList())
//            }
//        }
//    }
//
//    class CartItemAdapter(
//        val viewModel: CartViewModel,
//        var allExtras: List<Extra>
//        ) :
//        ListAdapter<CartItem, CartItemAdapter.Vh>(object : DiffUtil.ItemCallback<CartItem>() {
//            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
//                oldItem.expanded == newItem.expanded
//
//            @SuppressLint("DiffUtilEquals")
//            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
//                oldItem.expanded == newItem.expanded
//        }) {
//
//        override fun onCreateViewHolder(parent: ViewGroup, position: Int): Vh {
//            return Vh(
//                CartItemBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//        }
//
//        override fun onBindViewHolder(holder: Vh, position: Int) {
//            holder.bindTo(getItem(position), viewModel, allExtras, )
//        }
//
//        class Vh(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
//            fun bindTo(cartItem: CartItem, viewModel: CartViewModel, allExtras: List<Extra>){
//                binding.apply {
//                    this.iceCreamNameText.text = cartItem.iceCream.name.toString()
//
//                    loadExtras(binding.extrasLayout, cartItem, allExtras, viewModel)
//                    loadChosenExtras(binding.chosenExtrasLayout, cartItem, allExtras)
//                    openCloseLogic(cartItem, binding.root, viewModel)
//
//                    this.removeCartItemButton.setOnClickListener {
//                        viewModel.removeCartItem(cartItem)
//                    }
//                }
//            }
//
//            private fun openCloseLogic(cartItem: CartItem, root: ConstraintLayout, viewModel: CartViewModel) {
//                root.setOnClickListener { view ->
//                    viewModel.changeExpandedStatus(cartItem)
//                }
//            }
//
//            @SuppressLint("SetTextI18n")
//            private fun loadChosenExtras(
//                layout: LinearLayout,
//                cartItem: CartItem,
//                allExtras: List<Extra>
//            ) {
//                layout.visibility = if (cartItem.expanded.not()) LinearLayout.VISIBLE else LinearLayout.GONE
//
//                cartItem.extraItemIds.forEach { extraItemId ->
//                    val textView = TextView(layout.context)
//                    allExtras.forEach {extra ->
//                        extra.items.find {it.id == extraItemId }?.let {
//                            textView.setText("- " + it.name)
//                        }
//                    }
//
//                    layout.addView(textView)
//                }
//            }
//
//            @SuppressLint("SetTextI18n")
//            private fun loadExtras(extrasLayout: LinearLayout, cartItem: CartItem, allExtras: List<Extra>, viewModel: CartViewModel) {
//                print("ExtrasSize"+allExtras.size)
//                for (extra in allExtras) {
//                    val typeText = TextView(extrasLayout.context)
//                    typeText.setTextColor(extrasLayout.context.resources.getColor(R.color.white))
//                    typeText.setPadding(20, 20, 10, 10)
//                    extrasLayout.addView(typeText)
//
//                    if (extra.required){
//                        typeText.setText(extra.type +"*")
//                        val radioGroup = RadioGroup(extrasLayout.context)
//                        var oldSelectedTag = 0L
//                        for (extraItem in extra.items) {
//                            val radioButton = RadioButton(extrasLayout.context)
//                            radioButton.setTextColor(extrasLayout.context.resources.getColor(R.color.white))
//                            radioButton.setText(extraItem.price.toString() + "€ " + extraItem.name)
//                            radioButton.tag = extraItem.id
//                            radioGroup.addView(radioButton)
//                            if (cartItem.extraItemIds.contains(extraItem.id)) {
//                                radioGroup.check(radioButton.id)
//                                oldSelectedTag = extraItem.id
//                            }
//                        }
//
//                        if (radioGroup.checkedRadioButtonId == -1) {
//                            val rbutton: RadioButton = (radioGroup.getChildAt(0) as RadioButton)
//                            radioGroup.check(rbutton.id)
//                            viewModel.addOrRemoveExtraIdfromCart(cartItem, rbutton.tag as Long, true)
//                        }
//
//                        radioGroup.setOnCheckedChangeListener { radioGroupView: RadioGroup, selectedId: Int ->
//                            val selectedRadioButton = extrasLayout.findViewById(selectedId) as RadioButton
//                            viewModel.addOrRemoveExtraIdfromCart(cartItem, oldSelectedTag, false)
//                            viewModel.addOrRemoveExtraIdfromCart(cartItem, selectedRadioButton.getTag() as Long, true)
//                            oldSelectedTag = selectedRadioButton.getTag() as Long
//                        }
//
//                        extrasLayout.addView(radioGroup)
//                    } else {
//                        typeText.setText(extra.type)
//                        for (extraItem in extra.items) {
//                            val checkBox = CheckBox(extrasLayout.context)
//                            checkBox.setTextColor(extrasLayout.context.resources.getColor(R.color.white))
//                            checkBox.setText(extraItem.price.toString() + "€ " + extraItem.name)
//                            if (cartItem.extraItemIds.contains(extraItem.id)) {
//                                checkBox.isChecked = true
//                            }
//                            checkBox.setOnCheckedChangeListener { view: CompoundButton?, isChecked: Boolean ->
//                                viewModel.addOrRemoveExtraIdfromCart(cartItem, extraItem.id, isChecked)
//                            }
//                            extrasLayout.addView(checkBox)
//                        }
//                    }
//                    extrasLayout.visibility = if (cartItem.expanded) LinearLayout.VISIBLE else LinearLayout.GONE
//                }
//            }
//        }
//    }
}