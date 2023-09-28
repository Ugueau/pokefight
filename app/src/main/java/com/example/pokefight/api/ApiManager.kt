package com.example.pokefight.api

import android.util.Log
import com.example.pokefight.IDataSource
import com.example.pokefight.model.Pokemon
import com.example.pokefight.services.IPokemonService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

object ApiManager : IDataSource, IPokemonDataCallback{
    var pokemonService : IPokemonService? = null
    var pokemonList = ArrayList<Pokemon>()
    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    init {
        createRetrofitPokemon()
    }

    private fun createRetrofitPokemon() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofitPokemon = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokemonService = retrofitPokemon.create(IPokemonService::class.java)
    }

    override fun getData(from: Int?, to: Int?) {
        for (i in (from ?: 1) ..(to ?: 10)){
            getPokemonById(this,i)
        }
    }

    private fun getPokemonById(callBack: IPokemonDataCallback, id :Int) {

        val callPokemon: Call<Pokemon>? =
            ApiManager.pokemonService?.getPokemonById(id)
        callPokemon?.enqueue(object : Callback<Pokemon?> {

            override fun onResponse(call: Call<Pokemon?>, response: Response<Pokemon?>) {
                if (response.isSuccessful) {
                    val a: Pokemon? = response.body()
                    a?.let {  }
                    if(a != null){
                        callBack.getPokemonResponseSuccess(a)
                    }
                    else{
                        Log.e("onResponse", "Failed parsing JSON")
                    }
                } else {
                    Log.e("onResponse", "Not successfull : " + response.code())
                    callBack.getPokemonError("Error server response status was : " + response.code())
                }
            }

            override fun onFailure(call: Call<Pokemon?>, t: Throwable) {
                t.localizedMessage?.let { Log.e("onFailure", it) }
                callBack.getPokemonError("Request error : " + t.localizedMessage)
            }


        })
    }

    override fun getPokemonResponseSuccess(pokemon: Pokemon) {
        pokemonList.clear()
        pokemonList.add(pokemon)
        Log.i("onResponse", "Pokemon "+pokemon.id + ": "+ pokemon.name+" received")
    }

    override fun getPokemonError(message: String) {
        Log.e("onResponse error", message)
    }
}