package com.myproject.warkopgundar

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM User WHERE phoneNumber = :phoneNumber")
    suspend fun getUserByPhoneNumber(phoneNumber: String): User?

    @Query("SELECT * FROM User WHERE phoneNumber = :phoneNumber")
    fun getUserByPhoneNumberFlow(phoneNumber: String): Flow<User?>

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE User SET username = :userName, phoneNumber = :phoneNumber")
    suspend fun updateUserProfile(userName: String, phoneNumber: String): Int

    @Query("UPDATE User SET password = :password WHERE phoneNumber = :phoneNumber")
    suspend fun updatePassword(password: String, phoneNumber: String): Int

    @Delete
    suspend fun deleteUser(user: User)
}