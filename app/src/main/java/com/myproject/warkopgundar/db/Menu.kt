package com.myproject.warkopgundar.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

object MenuCategory {
    const val ALL = 0
    const val COFFE = 1
    const val MIE = 2
    const val RICE = 3
}

@Parcelize
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
): Parcelable