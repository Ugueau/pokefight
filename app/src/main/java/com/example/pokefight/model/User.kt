package com.example.pokefight.model

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
 data class User (
    @SerializedName("Email") @Expose var Email: String,
    @SerializedName("Nickname") @Expose var Nickname: String?,
    @SerializedName("trophy") @Expose var Trophy: Int?,
    @SerializedName("UserToken") @Expose val UserToken: String?
) {
     fun getDefaultUser():User{

         return User("Test@test.test", "Bob le bricolo", 4444, "0987654321")
     }

     fun getDefaultPassword():String{
         return "1234"
     }
}
