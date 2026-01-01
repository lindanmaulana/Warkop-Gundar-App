package com.myproject.warkopgundar

data class MenuModel(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val rating: String,
    val likes: String,
    val imageRes: Int,
    val category: String
)

object MenuCategory {
    const val COFFE = "COFFEE"
    const val MIE = "MIE"
    const val RICE = "RICE"
}