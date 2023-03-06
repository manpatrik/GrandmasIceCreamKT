package com.example.grandmasicecreamkt.CartF

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.databinding.ActivityCartBinding
import com.example.grandmasicecreamkt.databinding.CartItemBinding
import com.example.grandmasicecreamkt.di.viewModelModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment(){

    lateinit var adapter:CartItemAdapter
    lateinit var binding: ActivityCartBinding;
    //private val presenter: CartPresenterInterface by inject()
    public val viewModel: CartViewModel by viewModel()

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
                    loadChosenExtras(binding.chosenExtrasLayout, cartItem, allExtras, viewModel)
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
                allExtras: List<Extra>,
                viewModel: CartViewModel
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











//
//    private fun showCart(cartItems: List<CartItem>, extras: List<Extra>) {
//        binding.cartLayout.removeAllViews()
//        for (cartItem in cartItems) { // presenter.getCartItems()
//            val cartItemLayoutWithRemove = LinearLayout(context)
//            cartItemLayoutWithRemove.orientation = LinearLayout.HORIZONTAL
//            val removeButton = ImageButton(context)
//            removeButton.setImageResource(R.drawable.ic_delete)
//            cartItemLayoutWithRemove.addView(removeButton)
//            val cartItemLayout = LinearLayout(context)
//            cartItemLayout.orientation = LinearLayout.VERTICAL
//            cartItemLayout.setBackgroundColor(resources.getColor(R.color.red))
//            val layoutParams = LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            layoutParams.setMargins(0, 0, 0, 10)
//            cartItemLayout.layoutParams = layoutParams
//            cartItemLayout.setPadding(10, 10, 10, 10)
//            val nameTextView = TextView(context)
//            nameTextView.setText(cartItem.iceCream.name)
//            nameTextView.textSize = 20f
//            cartItemLayout.addView(nameTextView)
//            val extrasListLayout: LinearLayout = showExtrasList(cartItem, extras)
//            cartItemLayout.addView(extrasListLayout)
//            cartItemLayout.setOnClickListener(
//                cartItemLayoutOnClikListener(
//                    cartItemLayout,
//                    extrasListLayout,
//                    cartItem,
//                    cartItems,
//                    extras
//                )
//            )
//            cartItemLayoutWithRemove.addView(cartItemLayout)
//            binding.cartLayout.addView(cartItemLayoutWithRemove)
//            removeButton.setOnClickListener(
//                removeButtonOnClickListener(
//                    cartItemLayout,
//                    cartItem,
//                    cartItemLayoutWithRemove,
//                    extras
//                )
//            )
//        }
//    }
//
//    private fun removeButtonOnClickListener(
//        cartItemLayout: LinearLayout,
//        cartItem: CartItem,
//        cartItemLayoutWithRemove: View,
//        extras: List<Extra>
//    ): View.OnClickListener {
//        return View.OnClickListener { view: View? ->
//            if (shownExtrasCartItemLayout === cartItemLayout) {
//                shownExtrasCartItemLayout?.removeView(shownExtrasLayout)
//                shownExtrasLayout = null
//                shownExtrasCartItemLayout = null
//                val newExtrasListLayout: LinearLayout = showExtrasList(hiddenExtrasListCartItem, extras)
//                hiddenExtrasListLayout?.addView(newExtrasListLayout)
//                hiddenExtrasListLayout = null
//                hiddenExtrasListCartItem = null
//            }
//            viewModel.removeCartItem(cartItem) // presenter.getCartItems().remove(cartItem)
//            binding.cartLayout.removeView(cartItemLayoutWithRemove)
//        }
//    }
//
//    private fun cartItemLayoutOnClikListener(
//        cartItemLayout: LinearLayout,
//        extrasListLayout: LinearLayout,
//        cartItem: CartItem,
//        cartItems: List<CartItem>,
//        extras: List<Extra>
//    ): View.OnClickListener {
//        return View.OnClickListener { view: View? ->
//            if (shownExtrasCartItemLayout === cartItemLayout) {
//                shownExtrasCartItemLayout?.removeView(shownExtrasLayout)
//                shownExtrasLayout = null
//                shownExtrasCartItemLayout = null
//                val newExtrasListLayout: LinearLayout = showExtrasList(hiddenExtrasListCartItem, extras)
//                hiddenExtrasListLayout?.addView(newExtrasListLayout)
//                hiddenExtrasListLayout = null
//                hiddenExtrasListCartItem = null
//            } else {
//                if (shownExtrasLayout != null) {
//                    shownExtrasCartItemLayout?.removeView(shownExtrasLayout)
//                    val newExtrasListLayout: LinearLayout = showExtrasList(hiddenExtrasListCartItem,extras)
//                    hiddenExtrasListLayout?.addView(newExtrasListLayout)
//                    hiddenExtrasListLayout = null
//                    hiddenExtrasListCartItem = null
//                }
//                extrasListLayout.removeAllViews()
//                hiddenExtrasListLayout = extrasListLayout
//                hiddenExtrasListCartItem = cartItem
//                val extrasView: LinearLayout = showExtras(cartItem, cartItems, extras)
//                cartItemLayout.addView(extrasView)
//                shownExtrasLayout = extrasView
//                shownExtrasCartItemLayout = cartItemLayout
//            }
//        }
//    }

//    @SuppressLint("SetTextI18n")
//    private fun showRequiredExtras(extra: Extra, cartItem: CartItem?, cartItems: List<CartItem>): View {
//        val radioGroup = RadioGroup(context)
//        for (item in extra.items) {
//            val radioButton = RadioButton(context)
//            radioButton.setTextColor(resources.getColor(R.color.white))
//            radioButton.setText(item.price.toString() + "€ " + item.name)
//            radioButton.tag = item.id
//            radioGroup.addView(radioButton)
//            // presenter.getCartItems().get(presenter.getCartItems().indexOf(cartItem))
//            //                    .getExtraItemIds().contains(item.id)
//            if (cartItems.get(cartItems.indexOf(cartItem)).extraItemIds.contains(item.id)) {
//                radioGroup.check(radioButton.id)
//            }
//        }
//        radioGroup.setOnCheckedChangeListener { radioGroupView: RadioGroup, selectedId: Int ->
//            val cartItemId: Int = cartItems.indexOf(cartItem)
//            val selectedTag = 0L
////                findViewById<View>(selectedId).tag as Long
//            for (j in 0 until radioGroupView.childCount) {
//                if (radioGroupView.getChildAt(j).tag !== selectedTag) {
//                    cartItems.get(cartItemId)
//                        .removeExtraItemId(radioGroupView.getChildAt(j).tag as Long)
//                } else {
//                    cartItems.get(cartItemId).addExtraItemIds(selectedTag)
//                }
//            }
//        }
//        return radioGroup
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun showExtrasList(cartItem: CartItem?, extras: List<Extra>): LinearLayout {
//        val extrasListLayout = LinearLayout(context)
//        extrasListLayout.orientation = LinearLayout.VERTICAL
//        extrasListLayout.setPadding(40, 0, 0, 0)
//        if (cartItem != null) {
//            for (itemId in cartItem.extraItemIds) {
//                var item: Item? = null
//                extras.forEach {
//                    item = it.items.find {
//                        item -> item.id == itemId
//                    }
//                }
//
////                for (i in 0 until presenter.getExtras().size) {
////                    for (j in 0 until presenter.getExtras().get(i).getItems().size) {
////                        if (presenter.getExtras().get(i).getItems().get(j).id === itemId) {
////                            item = presenter.getExtras().get(i).getItems().get(j)
////                        }
////                    }
////                }
//
//                if (item != null) {
//                    val itemNameText = TextView(context)
//                    itemNameText.text = "- " + item?.name
//                    extrasListLayout.addView(itemNameText)
//                }
//            }
//        }
//        return extrasListLayout
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun showExtras(cartItem: CartItem?, cartItems: List<CartItem>, extras: List<Extra>): LinearLayout {
//        val extrasLayout = LinearLayout(context)
//        extrasLayout.orientation = LinearLayout.VERTICAL
//        val extras: List<Extra> = extras
//        for (extra in extras) {
//            val extraLayout = LinearLayout(context)
//            extraLayout.orientation = LinearLayout.VERTICAL
//            extraLayout.setBackgroundColor(resources.getColor(R.color.red))
//            val layoutParams = LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            layoutParams.setMargins(0, 20, 0, 0)
//            extraLayout.layoutParams = layoutParams
//            val typeText = TextView(context)
//            typeText.setTextColor(resources.getColor(R.color.white))
//            typeText.setPadding(20, 20, 10, 10)
//            extraLayout.addView(typeText)
//            if (extra.required) {
//                typeText.setText(extra.type + " *")
//                extraLayout.addView(showRequiredExtras(extra, cartItem, cartItems))
//            } else {
//                typeText.setText(extra.type)
//                for (item in extra.items) {
//                    extraLayout.addView(showOptionalExtra(item, cartItem, cartItems))
//                }
//            }
//            extrasLayout.addView(extraLayout)
//        }
//        return extrasLayout
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun showOptionalExtra(item: Item, cartItem: CartItem?, cartItems: List<CartItem>): View? {
//        val checkBox = CheckBox(context)
//        checkBox.setTextColor(resources.getColor(R.color.white))
//        checkBox.setText(item.price.toString() + "€ " + item.name)
//        if (cartItems.get(cartItems.indexOf(cartItem))
//                .extraItemIds.contains(item.id)
//        ) {
//            checkBox.isChecked = true
//        }
//        checkBox.setOnCheckedChangeListener { view: CompoundButton?, isChecked: Boolean ->
//            cartItems.get(
//                cartItems.indexOf(cartItem)
//            ).addOrRemoveExtraId(item.id, isChecked)
//        }
//        return checkBox
//    }


}