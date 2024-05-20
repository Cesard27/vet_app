package com.componentes.vet_app.view.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.componentes.vet_app.R
import com.componentes.vet_app.view.model.Pet
import com.componentes.vet_app.view.model.connect.PetService
import com.componentes.vet_app.view.model.connect.RetrofitClient
import com.componentes.vet_app.view.navigation.Screen
import com.componentes.vet_app.view.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NewPetScreen(navController: NavController){

    val typeList = listOf(
        "Perros",
        "Gatos",
        "Peces",
        "Roedores",
        "Anfibios",
        "Reptiles",
        "Áves",
        "Artrópodos"
    )

    var petName by remember { mutableStateOf("") }
    var petType by remember { mutableStateOf("") }
    var petAge by remember { mutableStateOf("") }
    var petBreed by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
        }
    }

    fun savePet() {
        val newPet = Pet(
            type = petType,
            name = petName,
            age = petAge.toIntOrNull() ?: 0,
            breed = petBreed,
            image = selectedImage,
            id = 0
        )
        val petService = RetrofitClient.instance.create(PetService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = petService.storePet(newPet)
                if (response.isSuccessful) {
                    // Mascota almacenada exitosamente
                    navController.navigate(Screen.PetDetails.route)
                    Log.d("bd", "Great!!!!!")
                } else {
                    //Toast.makeText(LocalContext.current, "Error al almacenar la mascota", Toast.LENGTH_SHORT).show()
                    Log.d("bd", "Error al almacenar la mascota")
                }
            } catch (e: Exception) {
                //Toast.makeText(LocalContext.current, "Error al almacenar la mascota", Toast.LENGTH_SHORT).show()
                Log.d("bd", "Error al almacenar la mascota")
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
            dropDownMenu(typeList)
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            //pet age
            textContent(stringResource(R.string.pet_age), false)
            customTextField()
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            // pet breed
            textContent(stringResource(R.string.pet_breed), false)
            customTextField()
            Spacer(modifier = Modifier.padding(vertical = 15.dp))
            //photo button
            customButton(
                text = stringResource(R.string.photo_button),
                white = true
            ) {
                getContent.launch("image/*")
            }

            // Mostrar la imagen seleccionada (si hay una)
            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }


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

