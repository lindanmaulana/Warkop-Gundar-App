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
    const val COFFE = "COFFEE"
    const val MIE = "MIE"
    const val RICE = "RICE"
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