package com.example.pokefight.model

import com.example.pokefight.domain.BDD.entity.UserBDD

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
