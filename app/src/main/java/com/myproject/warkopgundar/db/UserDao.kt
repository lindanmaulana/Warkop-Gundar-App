package com.myproject.warkopgundar.db

import androidx.room.*
import com.myproject.warkopgundar.db.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT EXISTS(SELECT 1 FROM Users WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM Users WHERE phoneNumber = :phone)")
    suspend fun isPhoneNumberExists(phone: String): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM Users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM Users WHERE email = :email")
    fun getUserByEmailFlow(email: String): Flow<User?>

    @Query("SELECT * FROM Users WHERE phoneNumber = :phone")
    suspend fun getUserByPhoneNumber(phone: String): User?

    @Query("SELECT * FROM Users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM Users WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE Users SET username = :userName WHERE email = :email")
    suspend fun updateUserProfile(userName: String, email: String): Int

    @Query("UPDATE Users SET password = :password WHERE email = :email")
    suspend fun updatePassword(password: String, email: String): Int

    @Delete
    suspend fun deleteUser(user: User)
}