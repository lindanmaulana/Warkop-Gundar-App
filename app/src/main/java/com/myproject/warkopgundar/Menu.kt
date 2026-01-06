package com.myproject.warkopgundar

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuModel(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val rating: String,
    val likes: String,
    val imageRes: Int,
    val category: String
): Parcelable

object MenuCategory {
    const val ALL = 0
    const val COFFE = 1
    const val MIE = 2
    const val RICE = 3
}

@Entity(
    tableName = "menus",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Menu(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Int,
    val rating: Double?,
    val likes: Int?,
    val imageRes: Int?,
    val categoryId: Int
)