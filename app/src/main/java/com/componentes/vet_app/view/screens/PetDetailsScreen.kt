package com.componentes.vet_app.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.componentes.vet_app.R
import com.componentes.vet_app.model.Pet
import com.componentes.vet_app.model.connect.PetService
import com.componentes.vet_app.view.navigation.Screen
import com.componentes.vet_app.view.ui.theme.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun PetDetailsScreen(navController: NavController, petId: Int){

    var pet by remember { mutableStateOf<Pet?>(null) }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.20.9:3030")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val petService = retrofit.create(PetService::class.java)

    LaunchedEffect(Unit) {
        try {
            val response = petService.getPetById(petId)
            if (response.isSuccessful) {
                pet = response.body()
            } else {
                // Manejar errores de la solicitud
                Log.e("PetDetailsScreen", "Failed to get pet details: ${response.message()}")
            }
        } catch (e: Exception) {
            // Manejar excepciones
            Log.e("PetDetailsScreen", "Exception: ${e.message}")
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(horizontal = 35.dp, vertical = 80.dp),
    ){
        Column() {
            pet?.let { pet ->
                // Mostrar los detalles reales del Pet
                textTitle(stringResource(R.string.pet_detail_name)+" ${pet.name}", false)

                Spacer(modifier = Modifier.padding(vertical = 20.dp))

                // Puedes mostrar otros detalles del Pet de manera similar
                textContent(stringResource(R.string.pet_detail_type) +" ${pet.type}", false)
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                textContent(stringResource(R.string.pet_detail_age)+" ${pet.age}", false)
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                textContent(stringResource(R.string.pet_detail_breed)+" ${pet.breed}", false)

                Spacer(modifier = Modifier.padding(vertical = 10.dp))

                // Aquí puedes mostrar la imagen del Pet si tienes la URL en el objeto Pet
                pet.image.let { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Pet Image",
                        modifier = Modifier
                            .size(width = 250.dp, height = 250.dp)
                            .clip(shape = RoundedCornerShape(16.dp))
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = 28.dp))

                navButton(
                    navController = navController,
                    text = stringResource(R.string.go_to_list_button),
                    white = true,
                    route = Screen.Home.route,
                    column = 1
                )
            }
        }
    }
}