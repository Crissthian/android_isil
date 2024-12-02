package com.example.imagenperro.data

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DogRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dog.ceo")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(DogAPICallable::class.java)

    fun fetchNewDogImage(onDogReceived: (Dog?) -> Unit) {
        api.getImage().enqueue(object : Callback<Dog> {
            override fun onResponse(call: Call<Dog>, response: Response<Dog>) {
                if (response.isSuccessful) {
                    onDogReceived(response.body())
                } else {
                    onDogReceived(null)
                }
            }

            override fun onFailure(call: Call<Dog>, t: Throwable) {
                Log.d("trace", "Error: ${t.message}")
                onDogReceived(null)
            }
        })
    }
}
