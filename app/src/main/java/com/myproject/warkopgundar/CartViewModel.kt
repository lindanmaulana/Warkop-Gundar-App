package com.myproject.warkopgundar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartWithMenu>>(emptyList())
    val cartItems: LiveData<List<CartWithMenu>> = _cartItems

    fun addToCart(menu: Menu, userId: Int) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.menu.id == menu.id && it.cart.userId == userId }

        if (index != -1) {
            val item = currentList[index]
            val updatedCart = item.cart.copy(quantity = item.cart.quantity + 1)
            val updatedItem = item.copy(cart = updatedCart)

            currentList[index] = updatedItem
        } else {
            val lastId = currentList.maxOfOrNull { it.cart.id } ?: 0
            val newCartItem = CartWithMenu(
                cart = Cart(id = lastId + 1, productId = menu.id, quantity = 1, userId = userId),
                menu = menu
            )
            currentList.add(newCartItem)
        }

        _cartItems.value = currentList.toList()
    }

    fun minusFromCart(menu: Menu, userId: Int) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.menu.id == menu.id && it.cart.userId == userId }

        if (index != -1) {
            val item = currentList[index]
            if (item.cart.quantity > 1) {
                val updatedCart = item.cart.copy(quantity = item.cart.quantity - 1)
                val updatedItem = item.copy(cart = updatedCart)

                currentList[index] = updatedItem
            } else {
                currentList.removeAt(index)
            }

            _cartItems.value = currentList.toList()
        }
    }

    fun removeFromCart(menu: Menu, userId: Int) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        currentList.removeAll { it.menu.id == menu.id && it.cart.userId == userId }
        _cartItems.value = currentList.toList()
    }

    fun getTotalPrice(): Int {
        return _cartItems.value?.sumOf { it.menu.price * it.cart.quantity } ?: 0
    }
}