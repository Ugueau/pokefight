package com.example.pokefight.domain.BDD.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

    @Query("Update UserBDD set  pokedollar = :newSolde where email = :email and password = :password")
    suspend fun updateSoldeUser(newSolde: Int, email: String, password: String)

    @Query("Select * from UserBDD where UserToken = :userToken")
    suspend fun findUserFromToken(userToken: String): UserBDD?

    @Query("UPDATE UserBDD SET Email = :email, Nickname = :nickname, trophy = :trophy, pokedollar = :pokedollar, UserToken = :userToken, Password = :password WHERE idUser = :userId")
    suspend fun forceUpdateUser(email : String, nickname : String, trophy : Int, pokedollar : Int, userToken : String, password : String, userId : Int)

    @Update
    suspend fun updateUser(userBDD: UserBDD)
}