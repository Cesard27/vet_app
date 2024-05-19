package com.componentes.vet_app.view.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.componentes.vet_app.R
import com.componentes.vet_app.view.model.Pet
import com.componentes.vet_app.view.model.connect.PetService
import com.componentes.vet_app.view.model.connect.RetrofitClient
import com.componentes.vet_app.view.navigation.Screen
import com.componentes.vet_app.view.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun NewPetScreen(navController: NavController){

    var petName by remember { mutableStateOf("") }
    var petType by remember { mutableStateOf("") }
    var petAge by remember { mutableStateOf("") }
    var petBreed by remember { mutableStateOf("") }

    // Función para guardar una nueva mascota
    fun savePet() {
        val newPet = Pet(
            type = petType,
            name = petName,
            age = petAge.toIntOrNull() ?: 0,
            breed = petBreed,
            image = "placeholder.jpg",
            id = 1
        )

        val petService = RetrofitClient.instance.create(PetService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = petService.storePet(newPet)
                if (response.isSuccessful) {
                    // Mascota almacenado exitosamente
                    navController.navigate(Screen.PetDetails.route)
                } else {
                    // Manejar el error
                }
            } catch (e: Exception) {
                // Manejar la excepción
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(horizontal = 35.dp, vertical = 80.dp),
    ){
        Column() {
            //title
            textTitle(stringResource(R.string.new_pet_button), false)
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            //pet name
            textContent(stringResource(R.string.pet_name), false)
            customTextField()
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            //pet type
            textContent(stringResource(R.string.pet_type), false)
            dropDownMenu()
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            //pet age
            textContent(stringResource(R.string.pet_age), false)
            customTextField()
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            // pet breed
            textContent(stringResource(R.string.pet_breed), false)
            dropDownMenu()
            Spacer(modifier = Modifier.padding(vertical = 15.dp))
            //photo button
            customButton(
                text = stringResource(R.string.photo_button),
                white = true
            ) { Toast.makeText(LocalContext.current, "photo", Toast.LENGTH_SHORT).show() }
            //save cancel buttons

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Row {
                navButton(
                    column = 2,
                    route = Screen.PetDetails.route,
                    navController = navController,
                    text = stringResource(R.string.save_button), white = true)
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                navButton(
                    column = 2,
                    route = Screen.Home.route,
                    navController = navController,
                    text = stringResource(R.string.cancel_button), white = true)

            }
        }


    }

}

