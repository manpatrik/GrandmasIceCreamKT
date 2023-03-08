package com.example.grandmasicecreamkt

import java.util.*

class CartItem(
    val id: String = UUID.randomUUID().toString(),
    var iceCream: IceCream,
    var extraItemIds: MutableList<Long> = mutableListOf(),
    var expanded: Boolean = false) {

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

    fun changeExpandedStatus() {
        expanded = expanded.not()
    }

    fun toCartEntity(): CartEntity {
        return CartEntity(id, iceCream.id, extraItemIds)
    }
}