package com.myproject.warkopgundar

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cart: Cart)

    @Transaction
    @Query("SELECT * FROM carts WHERE userId = :userId")
    fun getCartWithMenu(userId: Int): LiveData<List<CartWithMenu>>

    @Query("SELECT * FROM carts WHERE userId = :userId AND productId = :productId LIMIT 1")
    suspend fun getItemByProductAndUser(userId: Int, productId: Int): Cart?

    @Query("UPDATE carts SET quantity = :newQuantity WHERE id = :cartId")
    suspend fun updateQuantity(cartId: Int, newQuantity: Int)

    @Delete
    suspend fun deleteItem(cart: Cart)

    @Query("DELETE FROM carts WHERE userId = :userId")
    suspend fun clearCart(userId: Int)

    @Transaction
    @Query("SELECT * FROM carts")
    fun getAllCartComplete(): LiveData<List<CartWithMenuAndUser>>
}