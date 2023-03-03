package com.example.grandmasicecreamkt

public class Extra(
    var type: String,
    var required: Boolean,
    private var items: MutableList<Item> = ArrayList()
) {
//    var type: String
//        private set
//    var required: Boolean
//    private var items: MutableList<Item>
//
//    constructor(type: String, required: Boolean, items: MutableList<Item>) {
//        this.type = type
//        this.required = required
//        this.items = items
//    }
//
//    constructor(type: String, required: Boolean) {
//        this.type = type
//        this.required = required
//        items = ArrayList()
//    }

    fun addItem(item: Item) {
        items.add(item)
    }

    fun setItems(items: MutableList<Item>) {
        this.items = items
    }

    fun getItems(): List<Item> {
        return items
    }
}