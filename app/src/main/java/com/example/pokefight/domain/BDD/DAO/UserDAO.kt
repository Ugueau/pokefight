package com.example.pokefight.domain.BDD.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pokefight.domain.BDD.entity.UserBDD

@Dao
interface UserDAO {

    @Insert
    suspend fun insertUser(userBDD: UserBDD)

    @Delete
    suspend fun deleteUser(userBDD: UserBDD)

    @Query("SELECT * FROM UserBDD WHERE email = :email LIMIT 1")
    suspend fun findUserFromEmail(email: String): UserBDD?

    @Query("Select * from UserBDD where email = :email and password = :password")
    suspend fun findUserFromEmailAndPassword(email: String, password: String): UserBDD?
}