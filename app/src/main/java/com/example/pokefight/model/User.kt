package com.example.pokefight.model

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.example.pokefight.domain.BDD.entity.UserBDD
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serial

data class User (
    var Email: String,
    var Password: String,
    var Nickname: String,
    var Trophy: Int,
    var pokedollar: Int,
    val UserToken: String,
    val userId: Int?
) {
     fun toEntity(): UserBDD{
         val UserEntity = UserBDD(
             this.Email,
             this.Nickname,
             this.Trophy,
             this.pokedollar,
             this.UserToken,
             this.Password
         )
         return UserEntity
     }

     fun updateSoldeUser(value: Int){
         this.pokedollar += value
     }
}
