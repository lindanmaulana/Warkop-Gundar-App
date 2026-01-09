package com.myproject.warkopgundar.features.dashboard.fragments.carts

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.myproject.warkopgundar.db.Menu
import com.myproject.warkopgundar.db.User

@Entity(
    tableName = "carts",
    foreignKeys = [
        ForeignKey(
            entity = Menu::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["productId"]),
        Index(value = ["userId"])
    ])
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val userId: Int,
    var quantity: Int,
    val note: String? = ""
)

data class CartWithMenuAndUser(
    @Embedded
    val cart: Cart,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    ) val menu: Menu,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    ) val user: User
)

data class CartWithMenu(
    @Embedded
    val cart: Cart,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    ) val menu: Menu
)

data class CartWithUser(
    @Embedded
    val cart: Cart,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    ) val user: User
)