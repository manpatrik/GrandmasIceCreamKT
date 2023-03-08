package com.example.grandmasicecreamkt.CartF

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.databinding.ActivityCartBinding
import com.example.grandmasicecreamkt.databinding.CartItemBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment(){

    private lateinit var adapter:CartItemAdapter
    private lateinit var binding: ActivityCartBinding;
    private val viewModel: CartViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = ActivityCartBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cartLayout.apply {
            this.layoutManager = LinearLayoutManager(this.context)
        }

        viewModel.cartData.observe(viewLifecycleOwner){data ->
            binding.cartLayout.adapter = CartItemAdapter(viewModel, data.extras).also {
                this@CartFragment.adapter = it
                this@CartFragment.adapter.submitList(data.cartItems.toMutableList())
            }
        }
    }

    class CartItemAdapter(
        val viewModel: CartViewModel,
        var allExtras: List<Extra>
        ) :
        ListAdapter<CartItem, CartItemAdapter.Vh>(object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
                oldItem.expanded == newItem.expanded

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
                oldItem.expanded == newItem.expanded
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, position: Int): Vh {
            return Vh(
                CartItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: Vh, position: Int) {
            holder.bindTo(getItem(position), viewModel, allExtras, )
        }

        class Vh(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bindTo(cartItem: CartItem, viewModel: CartViewModel, allExtras: List<Extra>){
                binding.apply {
                    this.iceCreamNameText.text = cartItem.iceCream.name.toString()

                    loadExtras(binding.extrasLayout, cartItem, allExtras, viewModel)
                    loadChosenExtras(binding.chosenExtrasLayout, cartItem, allExtras)
                    openCloseLogic(cartItem, binding.root, viewModel)

                    this.removeCartItemButton.setOnClickListener {
                        viewModel.removeCartItem(cartItem)
                    }
                }
            }

            private fun openCloseLogic(cartItem: CartItem, root: ConstraintLayout, viewModel: CartViewModel) {
                root.setOnClickListener { view ->
                    viewModel.changeExpandedStatus(cartItem)
                }
            }

            @SuppressLint("SetTextI18n")
            private fun loadChosenExtras(
                layout: LinearLayout,
                cartItem: CartItem,
                allExtras: List<Extra>
            ) {
                layout.visibility = if (cartItem.expanded.not()) LinearLayout.VISIBLE else LinearLayout.GONE

                cartItem.extraItemIds.forEach { extraItemId ->
                    val textView = TextView(layout.context)
                    allExtras.forEach {extra ->
                        extra.items.find {it.id == extraItemId }?.let {
                            textView.setText("- " + it.name)
                        }
                    }

                    layout.addView(textView)
                }
            }

            @SuppressLint("SetTextI18n")
            private fun loadExtras(extrasLayout: LinearLayout, cartItem: CartItem, allExtras: List<Extra>, viewModel: CartViewModel) {
                print("ExtrasSize"+allExtras.size)
                for (extra in allExtras) {
                    val typeText = TextView(extrasLayout.context)
                    typeText.setTextColor(extrasLayout.context.resources.getColor(R.color.white))
                    typeText.setPadding(20, 20, 10, 10)
                    extrasLayout.addView(typeText)

                    if (extra.required){
                        typeText.setText(extra.type +"*")
                        val radioGroup = RadioGroup(extrasLayout.context)
                        var oldSelectedTag = 0L
                        for (extraItem in extra.items) {
                            val radioButton = RadioButton(extrasLayout.context)
                            radioButton.setTextColor(extrasLayout.context.resources.getColor(R.color.white))
                            radioButton.setText(extraItem.price.toString() + "€ " + extraItem.name)
                            radioButton.tag = extraItem.id
                            radioGroup.addView(radioButton)
                            if (cartItem.extraItemIds.contains(extraItem.id)) {
                                radioGroup.check(radioButton.id)
                                oldSelectedTag = extraItem.id
                            }
                        }

                        if (radioGroup.checkedRadioButtonId == -1) {
                            val rbutton: RadioButton = (radioGroup.getChildAt(0) as RadioButton)
                            radioGroup.check(rbutton.id)
                            viewModel.addOrRemoveExtraIdfromCart(cartItem, rbutton.tag as Long, true)
                        }

                        radioGroup.setOnCheckedChangeListener { radioGroupView: RadioGroup, selectedId: Int ->
                            val selectedRadioButton = extrasLayout.findViewById(selectedId) as RadioButton
                            viewModel.addOrRemoveExtraIdfromCart(cartItem, oldSelectedTag, false)
                            viewModel.addOrRemoveExtraIdfromCart(cartItem, selectedRadioButton.getTag() as Long, true)
                            oldSelectedTag = selectedRadioButton.getTag() as Long
                        }

                        extrasLayout.addView(radioGroup)
                    } else {
                        typeText.setText(extra.type)
                        for (extraItem in extra.items) {
                            val checkBox = CheckBox(extrasLayout.context)
                            checkBox.setTextColor(extrasLayout.context.resources.getColor(R.color.white))
                            checkBox.setText(extraItem.price.toString() + "€ " + extraItem.name)
                            if (cartItem.extraItemIds.contains(extraItem.id)) {
                                checkBox.isChecked = true
                            }
                            checkBox.setOnCheckedChangeListener { view: CompoundButton?, isChecked: Boolean ->
                                viewModel.addOrRemoveExtraIdfromCart(cartItem, extraItem.id, isChecked)
                            }
                            extrasLayout.addView(checkBox)
                        }
                    }
                    extrasLayout.visibility = if (cartItem.expanded) LinearLayout.VISIBLE else LinearLayout.GONE
                }
            }
        }
    }
}