package com.example.grandmasicecreamkt

public class Extra {
    var type: String
        private set
    var required: Boolean
    private var items: MutableList<Item>

    constructor(type: String) {
        this.type = type
        required = false
        items = ArrayList()
    }

    constructor(type: String, required: Boolean) {
        this.type = type
        this.required = required
        items = ArrayList()
    }

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