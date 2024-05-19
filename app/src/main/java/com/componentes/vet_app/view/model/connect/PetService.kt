package com.componentes.vet_app.view.model.connect

import com.componentes.vet_app.view.model.Pet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PetService {
    @POST("/pets")
    suspend fun storePet(@Body pet: Pet): Response<Void>

    @GET("/pets")
    suspend fun getAllPets(): Response<List<Pet>>

    @GET("/pets/filter")
    suspend fun filterPetsByNameAndSortByBreed(
        @Query("name") name: String?,
        @Query("sortBy") sortBy: String?
    ): Response<List<Pet>>
}
