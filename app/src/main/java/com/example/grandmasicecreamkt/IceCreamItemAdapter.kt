package com.example.grandmasicecreamkt

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grandmasicecreamkt.IceCreams.IceCreamsPresenterInterface
import java.net.URL

class IceCreamItemAdapter(
    private val context: Context,
    val iceCreams: List<IceCream>,
    private val presenter: IceCreamsPresenterInterface
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
            val loadImageThread: Thread = object : Thread() {
                override fun run() {
                    if (iceCream.imageUrl != "") {
                        try {
                            val newurl = URL(iceCream.imageUrl)
                            val mIcon_val =
                                BitmapFactory.decodeStream(newurl.openConnection().getInputStream())
                            iceCreamImage.setImageBitmap(mIcon_val)
                        } catch (e: Exception) {
                            println("ERROR: $e")
                        }
                    }
                }
            }
            loadImageThread.start()
            toCartButton.setOnClickListener { view: View? ->
                presenter.addCartItem(CartItem(iceCream, mutableListOf()))
            }
        }
    }
}