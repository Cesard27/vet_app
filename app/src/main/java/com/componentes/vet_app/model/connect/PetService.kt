package com.componentes.vet_app.model.connect

import android.content.Context
import android.net.Uri
import com.componentes.vet_app.model.Pet
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface PetService {

    @Multipart
    @POST("/pets")
    suspend fun uploadPet(
        @Part("type") type: RequestBody,
        @Part("name") name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Respuesta>



    @POST("/pets")
    suspend fun storePet(@Body pet: Pet): Response<Void>

    @GET("/pets")
    suspend fun getAllPets(): Response<List<Pet>>

    @GET("/pets/filter")
    suspend fun getPetByName(
        @Query("name") name: String?,
        //@Query("sortBy") sortBy: String?
    ): Response<List<Pet>>
    @GET("/pets/{id}")
    suspend fun getPetById(
        @Path("id") id: Int
    ): Response<Pet>

}
