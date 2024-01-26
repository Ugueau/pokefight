package com.example.pokefight.VIewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokefight.model.User

class UserViewModel: ViewModel() {

    var user : User? = null

    fun valoriseUser(newUser: User){
        user = newUser
    }

}