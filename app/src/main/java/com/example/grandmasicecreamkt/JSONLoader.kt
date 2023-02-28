package com.example.grandmasicecreamkt

import android.app.Activity
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.lang.Boolean
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.Any
import kotlin.Exception
import kotlin.String
import kotlin.also

object JSONLoader {
    private fun loadJSON(url: URL): String {
//        return try {
//            val connection = url.openConnection() as HttpURLConnection
//            connection.requestMethod = "GET"
//            connection.connect()
//            val stream = connection.inputStream
//            val reader = BufferedReader(InputStreamReader(stream))
//            val buffer = StringBuffer()
//            var line = ""
//            while (reader.readLine().also { line = it } != null) {
//                buffer.append(
//                    """
//                        $line
//
//                        """.trimIndent()
//                )
//            }
//            connection.disconnect()
//            buffer.toString()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
        var jsonString: String = ""
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    jsonString.plus(line)
                    print(line)
                }
            }
            return jsonString
        }

    }

    fun loadExtrasFromJson(activity: Activity?): List<Extra>? {
        var extrasString: String? = null
        return try {
            extrasString =
                loadJSON(URL("https://raw.githubusercontent.com/udemx/hr-resources/master/extras.json"))
            val jsonParser = JSONParser()
            val extrasJsonObject: Any = jsonParser.parse(extrasString)
            val extrasJson: JSONArray = extrasJsonObject as JSONArray
            val extras: MutableList<Extra> = ArrayList<Extra>()
            if (extrasJson != null) {
                for (`object` in extrasJson) {
                    val extraJson: JSONObject = `object` as JSONObject
                    val extra = Extra(extraJson.get("type").toString())
                    if (extraJson.get("required") != null) {
                        extra.required = (
                            Boolean.parseBoolean(
                                extraJson.get("required").toString()
                            )
                        )
                    }
                    val itemsJson: JSONArray = extraJson.get("items") as JSONArray
                    extra.setItems(itemsFromJSONArray(itemsJson) as MutableList<Item>)
                    extras.add(extra)
                }
            }
//            Collections.sort(extras,
//                Comparator<T> { s1: T, s2: T ->
//                    Boolean.compare(
//                        s2.getRequired(),
//                        s1.getRequired()
//                    )
//                })
            extras
        } catch (e: Exception) {
            println("ERROR: $e")
            null
        }
    }

    private fun itemsFromJSONArray(itemsJson: JSONArray): List<Item> {
        val items: MutableList<Item> = ArrayList<Item>()
        for (`object` in itemsJson) {
            val itemJson: JSONObject = `object` as JSONObject
            val item = Item(
                itemJson.get("id").toString().toLong(),
                itemJson.get("name").toString(), itemJson.get("price").toString().toDouble()
            )
            items.add(item)
        }
        return items
    }

    fun loadIceCreamsFromJson(activity: Activity?) /*: List<IceCream>?*/ {
        val iceCreamsString = loadJSON(URL("https://raw.githubusercontent.com/udemx/hr-resources/master/icecreams.json"))
        print(iceCreamsString)
        print("valami")

//        return try {
//            val iceCreamsString = loadJSON(URL("https://raw.githubusercontent.com/udemx/hr-resources/master/icecreams.json"))
//            print(iceCreamsString)
//            print("valami")
//            val iceCreams: MutableList<IceCream> = ArrayList<IceCream>()
//            val jsonParser = JSONParser()
//            val `object`: Any = jsonParser.parse(iceCreamsString)
//            val iceCreamsJson: JSONObject = `object` as JSONObject
//            if (iceCreamsJson != null) {
//                val iceCreamTypes: JSONArray = iceCreamsJson.get("iceCreams") as JSONArray
//                for (i in 0 until iceCreamTypes.size) {
//                    val iceCream: JSONObject = iceCreamTypes.get(i) as JSONObject
////                    var imgUrl: String?
////                    var url: Any
////                    if (iceCream.get("imageUrl").also {
////                            if (it != null) {
////                                url = it
////                            }
////                        } != null) {
////                        imgUrl = url.toString()
////                    } else {
////                        imgUrl = null
////                    }
//                    iceCreams.add(
//                        IceCream(
//                            iceCream.get("id").toString().toLong(),
//                            iceCream.get("name").toString(),
//                            iceCream.get("status").toString(),
//                            "https://github.com/udemx/software-test/blob/main/mobile-development/assets/placeholder.jpg"
//                        )
//                    )
//                }
////                Collections.sort(iceCreams,
////                    Comparator<T> { s1: T, s2: T ->
////                        s1.getStatus().compareTo(s2.getStatus())
////                    })
//            }
//            iceCreams
//        } catch (e: Exception) {
//            println("ERROR: $e")
//            null
//        }
//    }
    }
}