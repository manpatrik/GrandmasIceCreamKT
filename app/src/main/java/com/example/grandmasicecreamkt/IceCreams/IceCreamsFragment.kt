package com.example.grandmasicecreamkt.IceCreams

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.grandmasicecreamkt.CartF.ui.theme.GrandmasIceCreamKTTheme
import com.example.grandmasicecreamkt.CartItem
import com.example.grandmasicecreamkt.IceCream
import com.example.grandmasicecreamkt.R
import com.example.grandmasicecreamkt.databinding.ActivityIceCreamsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class IceCreamsFragment : Fragment() {

    // private lateinit var binding: ActivityIceCreamsBinding;
    private val viewModel: IceCreamsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = ActivityIceCreamsBinding.inflate(inflater, container, false).apply {
        // binding = this
        this.root.setContent {
            val iceCreams = viewModel.iceCreams.value
            ShowIceCreams(iceCreams)
        }
    }.root

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ShowIceCreams(iceCreams: List<IceCream>) {
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
                    },
                    actions = {
                        IconButton(
                            onClick = { findNavController().navigate(IceCreamsFragmentDirections.actionIceCreamsActivityToCartFragment()) }
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_cart_outline), contentDescription = "kosár", tint = Color.White)
                        }
                    }
                )
            }
        ) {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(items = iceCreams){iceCream ->
                    Column(Modifier.fillMaxWidth()) {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = ImageRequest.Builder(LocalContext.current).data(iceCream.imageUrl).crossfade(true).build(),
                            placeholder = painterResource(id = R.drawable.placeholder),
                            error = painterResource(id = R.drawable.placeholder),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = iceCream.name
                        )

                        Row(
                            modifier = Modifier
                                .background(colorResource(id = R.color.red))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.fillMaxHeight()){
                                Text(
                                    text = iceCream.name.toUpperCase(),
                                    color = colorResource(id = R.color.yellow),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                )
                                Text(
                                    text = "1 €",
                                    color = colorResource(id = R.color.white),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            Button(
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
                                shape = RoundedCornerShape(25),
                                onClick = { viewModel.addCartItem(CartItem(iceCream = iceCream, extraItemIds = mutableListOf())) },
                            ) {
                                Text(
                                    text = "KOSÁRBA",
                                    color = colorResource(id = R.color.red),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(10.dp, 0.dp)
                                )
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
            ShowIceCreams(listOf(
                IceCream(1L, "vanilia", IceCream.Status.AVAILABLE, ""),
                IceCream(2L, "Pisztácia", IceCream.Status.AVAILABLE, ""),
                IceCream(3L, "Csoki", IceCream.Status.AVAILABLE, "")
            ))
        }
    }
}