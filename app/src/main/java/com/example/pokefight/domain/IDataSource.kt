package com.example.pokefight.domain

interface IDataSource {

    suspend fun getData(from : Int?, to : Int?)

}