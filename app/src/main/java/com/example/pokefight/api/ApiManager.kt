package com.example.pokefight.api
import com.example.pokefight.model.Pokemon
import com.example.pokefight.services.PokemonService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

object ApiManager {
    private var pokemonService : PokemonService? = null
    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    init {
        createRetrofitPokemon()
    }

    private fun createRetrofitPokemon(): PokemonService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofitPokemon = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitPokemon.create(PokemonService::class.java)
    }
}