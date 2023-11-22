package com.example.pokefight.domain.api

import com.example.pokefight.domain.IDataSource
import com.example.pokefight.model.Pokemon
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

object DSRetrofit : IDataSource {

    var pokemonList = ArrayList<Pokemon>()
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val interceptor = HttpLoggingInterceptor().apply {  level = HttpLoggingInterceptor.Level.BODY }
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofitPokemon: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val pokemonService : IPokemonService = retrofitPokemon.create(IPokemonService::class.java)


    override suspend fun getData(from: Int?, to: Int?) {
        TODO("Not yet implemented")
    }

}