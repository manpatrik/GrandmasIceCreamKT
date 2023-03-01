package com.example.grandmasicecreamkt.CartF

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.databinding.ActivityCartBinding
import org.koin.android.ext.android.inject

class CartFragment : Fragment(){

    var shownExtrasLayout: LinearLayout? = null
    var shownExtrasCartItemLayout: LinearLayout? = null

    var hiddenExtrasListLayout: LinearLayout? = null
    var hiddenExtrasListCartItem: CartItem? = null

    lateinit var binding: ActivityCartBinding;
    private val presenter: CartPresenterInterface by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = ActivityCartBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCart()
    }

    private fun showCart() {
        for (cartItem in presenter.getCartItems()) { // presenter.getCartItems()
            val cartItemLayoutWithRemove = LinearLayout(context)
            cartItemLayoutWithRemove.orientation = LinearLayout.HORIZONTAL
            val removeButton = ImageButton(context)
            removeButton.setImageResource(R.drawable.ic_delete)
            cartItemLayoutWithRemove.addView(removeButton)
            val cartItemLayout = LinearLayout(context)
            cartItemLayout.orientation = LinearLayout.VERTICAL
            cartItemLayout.setBackgroundColor(resources.getColor(R.color.red))
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 0, 10)
            cartItemLayout.layoutParams = layoutParams
            cartItemLayout.setPadding(10, 10, 10, 10)
            val nameTextView = TextView(context)
            nameTextView.setText(cartItem.iceCream.name)
            nameTextView.textSize = 20f
            cartItemLayout.addView(nameTextView)
            val extrasListLayout: LinearLayout = showExtrasList(cartItem)
            cartItemLayout.addView(extrasListLayout)
            cartItemLayout.setOnClickListener(
                cartItemLayoutOnClikListener(
                    cartItemLayout,
                    extrasListLayout,
                    cartItem
                )
            )
            cartItemLayoutWithRemove.addView(cartItemLayout)
            binding.cartLayout.addView(cartItemLayoutWithRemove)
            removeButton.setOnClickListener(
                removeButtonOnClickListener(
                    cartItemLayout,
                    cartItem,
                    cartItemLayoutWithRemove
                )
            )
        }
    }

    private fun removeButtonOnClickListener(
        cartItemLayout: LinearLayout,
        cartItem: CartItem,
        cartItemLayoutWithRemove: View
    ): View.OnClickListener {
        return View.OnClickListener { view: View? ->
            if (shownExtrasCartItemLayout === cartItemLayout) {
                shownExtrasCartItemLayout?.removeView(shownExtrasLayout)
                shownExtrasLayout = null
                shownExtrasCartItemLayout = null
                val newExtrasListLayout: LinearLayout = showExtrasList(hiddenExtrasListCartItem)
                hiddenExtrasListLayout?.addView(newExtrasListLayout)
                hiddenExtrasListLayout = null
                hiddenExtrasListCartItem = null
            }
            presenter.removeCartItem(cartItem) // presenter.getCartItems().remove(cartItem)
            binding.cartLayout.removeView(cartItemLayoutWithRemove)
        }
    }

    private fun cartItemLayoutOnClikListener(
        cartItemLayout: LinearLayout,
        extrasListLayout: LinearLayout,
        cartItem: CartItem
    ): View.OnClickListener {
        return View.OnClickListener { view: View? ->
            if (shownExtrasCartItemLayout === cartItemLayout) {
                shownExtrasCartItemLayout?.removeView(shownExtrasLayout)
                shownExtrasLayout = null
                shownExtrasCartItemLayout = null
                val newExtrasListLayout: LinearLayout = showExtrasList(hiddenExtrasListCartItem)
                hiddenExtrasListLayout?.addView(newExtrasListLayout)
                hiddenExtrasListLayout = null
                hiddenExtrasListCartItem = null
            } else {
                if (shownExtrasLayout != null) {
                    shownExtrasCartItemLayout?.removeView(shownExtrasLayout)
                    val newExtrasListLayout: LinearLayout = showExtrasList(hiddenExtrasListCartItem)
                    hiddenExtrasListLayout?.addView(newExtrasListLayout)
                    hiddenExtrasListLayout = null
                    hiddenExtrasListCartItem = null
                }
                extrasListLayout.removeAllViews()
                hiddenExtrasListLayout = extrasListLayout
                hiddenExtrasListCartItem = cartItem
                val extrasView: LinearLayout = showExtras(cartItem)
                cartItemLayout.addView(extrasView)
                shownExtrasLayout = extrasView
                shownExtrasCartItemLayout = cartItemLayout
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showRequiredExtras(extra: Extra, cartItem: CartItem?): View {
        val radioGroup = RadioGroup(context)
        for (item in extra.getItems()) {
            val radioButton = RadioButton(context)
            radioButton.setTextColor(resources.getColor(R.color.white))
            radioButton.setText(item.price.toString() + "€ " + item.name)
            radioButton.tag = item.id
            radioGroup.addView(radioButton)
            // presenter.getCartItems().get(presenter.getCartItems().indexOf(cartItem))
            //                    .getExtraItemIds().contains(item.id)
            if (presenter.isCartItemContainExtraItem(cartItem, item.id)) {
                radioGroup.check(radioButton.id)
            }
        }
        radioGroup.setOnCheckedChangeListener { radioGroupView: RadioGroup, selectedId: Int ->
            val cartItemId: Int = presenter.getCartItems().indexOf(cartItem)
            val selectedTag = 0L
//                findViewById<View>(selectedId).tag as Long
            for (j in 0 until radioGroupView.childCount) {
                if (radioGroupView.getChildAt(j).tag !== selectedTag) {
                    presenter.getCartItems().get(cartItemId)
                        .removeExtraItemId(radioGroupView.getChildAt(j).tag as Long)
                } else {
                    presenter.getCartItems().get(cartItemId).addExtraItemIds(selectedTag)
                }
            }
        }
        return radioGroup
    }

    @SuppressLint("SetTextI18n")
    private fun showExtrasList(cartItem: CartItem?): LinearLayout {
        val extrasListLayout = LinearLayout(context)
        extrasListLayout.orientation = LinearLayout.VERTICAL
        extrasListLayout.setPadding(40, 0, 0, 0)
        if (cartItem != null) {
            for (itemId in cartItem.extraItemIds) {
                var item: Item? = null
                presenter.getExtras().forEach {
                    item = it.getItems().find {
                        item -> item.id == itemId
                    }
                }

//                for (i in 0 until presenter.getExtras().size) {
//                    for (j in 0 until presenter.getExtras().get(i).getItems().size) {
//                        if (presenter.getExtras().get(i).getItems().get(j).id === itemId) {
//                            item = presenter.getExtras().get(i).getItems().get(j)
//                        }
//                    }
//                }

                if (item != null) {
                    val itemNameText = TextView(context)
                    itemNameText.text = "- " + item?.name
                    extrasListLayout.addView(itemNameText)
                }
            }
        }
        return extrasListLayout
    }

    @SuppressLint("SetTextI18n")
    private fun showExtras(cartItem: CartItem?): LinearLayout {
        val extrasLayout = LinearLayout(context)
        extrasLayout.orientation = LinearLayout.VERTICAL
        val extras: List<Extra> = presenter.getExtras()
        for (extra in extras) {
            val extraLayout = LinearLayout(context)
            extraLayout.orientation = LinearLayout.VERTICAL
            extraLayout.setBackgroundColor(resources.getColor(R.color.red))
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 20, 0, 0)
            extraLayout.layoutParams = layoutParams
            val typeText = TextView(context)
            typeText.setTextColor(resources.getColor(R.color.white))
            typeText.setPadding(20, 20, 10, 10)
            extraLayout.addView(typeText)
            if (extra.required) {
                typeText.setText(extra.type + " *")
                extraLayout.addView(showRequiredExtras(extra, cartItem))
            } else {
                typeText.setText(extra.type)
                for (item in extra.getItems()) {
                    extraLayout.addView(showOptionalExtra(item, cartItem))
                }
            }
            extrasLayout.addView(extraLayout)
        }
        return extrasLayout
    }

    @SuppressLint("SetTextI18n")
    private fun showOptionalExtra(item: Item, cartItem: CartItem?): View? {
        val checkBox = CheckBox(context)
        checkBox.setTextColor(resources.getColor(R.color.white))
        checkBox.setText(item.price.toString() + "€ " + item.name)
        if (presenter.getCartItems().get(presenter.getCartItems().indexOf(cartItem))
                .extraItemIds.contains(item.id)
        ) {
            checkBox.isChecked = true
        }
        checkBox.setOnCheckedChangeListener { view: CompoundButton?, isChecked: Boolean ->
            presenter.getCartItems().get(
                presenter.getCartItems().indexOf(cartItem)
            ).addOrRemoveExtraId(item.id, isChecked)
        }
        return checkBox
    }


}