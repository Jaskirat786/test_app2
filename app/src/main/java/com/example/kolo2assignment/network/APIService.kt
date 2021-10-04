package com.example.kolo2assignment.network
import com.example.kolo2assignment.comic_model.Characters
import com.example.kolo2assignment.comic_model.Comics;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

 interface APIService {
     @GET("characters?")
     fun getCharacter(
         @Query("ts")  ts:String,
         @Query("apikey") apiKey:String,
         @Query("hash")      hash:String
     ):Call<Characters>

    @GET("comics?")
    fun getComics(
        @Query("ts")  ts:String,
        @Query("apikey") apiKey:String,
        @Query("hash")      hash:String
    ):Call<Comics>
}