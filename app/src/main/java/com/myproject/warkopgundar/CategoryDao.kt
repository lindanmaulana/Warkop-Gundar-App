package com.myproject.warkopgundar

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesWithMenu(): Flow<List<CategoryWithMenu>>

    @Insert
    suspend fun insertCategory(category: Category)
}