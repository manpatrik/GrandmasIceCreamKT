package com.example.grandmasicecreamkt

class CartItem(var iceCream: IceCream, var extraItemIds: MutableList<Long>, var extended: Boolean = false) {

    fun addExtraItemIds(itemId: Long) {
        if (!extraItemIds.contains(itemId)) {
            extraItemIds.add(itemId)
        }
    }

    fun removeExtraItemId(extraItemId: Long) {
        extraItemIds.remove(extraItemId)
    }

    fun addOrRemoveExtraId(id: Long, isChecked: Boolean) {
        if (isChecked) {
            addExtraItemIds(id)
        } else {
            removeExtraItemId(id)
        }
    }
}