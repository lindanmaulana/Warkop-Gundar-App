package com.myproject.warkopgundar

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM Users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM Users WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM Users WHERE phoneNumber = :phoneNumber")
    suspend fun getUserByPhoneNumber(phoneNumber: String): User?

    @Query("SELECT * FROM Users WHERE phoneNumber = :phoneNumber")
    fun getUserByPhoneNumberFlow(phoneNumber: String): Flow<User?>

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE Users SET username = :userName, phoneNumber = :phoneNumber")
    suspend fun updateUserProfile(userName: String, phoneNumber: String): Int

    @Query("UPDATE Users SET password = :password WHERE phoneNumber = :phoneNumber")
    suspend fun updatePassword(password: String, phoneNumber: String): Int

    @Delete
    suspend fun deleteUser(user: User)
}