package br.senai.sp.jandira.formativa.retrofit

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("/usuario/cadastrarUsuario")
    suspend fun createUser(@Body body: JsonObject): Response<JsonObject>
}