package com.myproject.warkopgundar

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: Menu)

    @Query("SELECT * FROM menu")
    fun getAllMenu(): Flow<List<Menu>>

    @Query("SELECT * FROM menu WHERE categoryId = :catId")
    fun getMenuByCategory(catId: Int): Flow<List<Menu>>

    @Query("SELECT * FROM menu WHERE name LIKE '%' || :search || '%'")
    fun searchMenu(search: String): Flow<List<Menu>>

    @Delete
    suspend fun deleteMenu(menu: Menu)

    @Query("DELETE FROM menu")
    suspend fun clearMenu()
}