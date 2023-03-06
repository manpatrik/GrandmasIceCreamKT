package com.example.grandmasicecreamkt

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.grandmasicecreamkt.IceCreams.IceCreamsViewModel

class IceCreamItemAdapter(
    private val context: Context,
    val iceCreams: List<IceCream>,
    private val viewModel: IceCreamsViewModel
) :
    RecyclerView.Adapter<IceCreamItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            )
                .inflate(R.layout.ice_cream_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(iceCreams[position])
    }

    override fun getItemCount(): Int {
        return iceCreams.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iceCreamName: TextView
        var iceCreamPrice: TextView
        var iceCreamImage: ImageView
        var toCartButton: Button

        init {
            iceCreamName = itemView.findViewById<TextView>(R.id.iceCreamName)
            iceCreamPrice = itemView.findViewById<TextView>(R.id.iceCreamPrice)
            iceCreamImage = itemView.findViewById<ImageView>(R.id.iceCreamImage)
            toCartButton = itemView.findViewById<Button>(R.id.toCart)
        }

        @SuppressLint("SetTextI18n")
        fun bindTo(iceCream: IceCream) {
            iceCreamName.setText(iceCream.name)
            if (iceCream.status == IceCream.Status.MELTED) {
                iceCreamPrice.text = "Kifogyott"
                toCartButton.isEnabled = false
                toCartButton.setTextColor(itemView.resources.getColor(R.color.grey))
            } else if (iceCream.status == IceCream.Status.UNAVAILABLE) {
                iceCreamPrice.text = "Nem is volt"
                toCartButton.isEnabled = false
                toCartButton.setTextColor(itemView.resources.getColor(R.color.grey))
            }
            if (iceCream.imageUrl.isNotEmpty()) {
                iceCreamImage.load(iceCream.imageUrl)
            }

            toCartButton.setOnClickListener { view: View? ->
                viewModel.addCartItem(CartItem(iceCream))
            }
        }
    }
}