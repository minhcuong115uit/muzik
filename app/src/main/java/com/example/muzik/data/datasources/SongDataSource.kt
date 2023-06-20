package com.example.muzik.data.datasources

import retrofit2.http.GET


interface RemoteApi {
    @GET("/songs")
    suspend fun getSongs(){
    }
}

class SongDataSource {

}