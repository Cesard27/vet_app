package com.componentes.vet_app.view.screens

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import com.componentes.vet_app.R
import com.componentes.vet_app.view.model.Pet
import com.componentes.vet_app.view.model.connect.PetService
import com.componentes.vet_app.view.model.connect.RetrofitClient
import com.componentes.vet_app.view.navigation.Screen
import com.componentes.vet_app.view.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun NewPetScreen(navController: NavController) {
    val context = LocalContext.current // Necesitas el contexto para acceder a los recursos

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
    var selectedType by remember { mutableStateOf(typeList[0]) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
            }
        }

    fun getRealPathFromURI(context: Context, uri: Uri?): String? {
        val result: String?
        val cursor = context.contentResolver.query(uri!!, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun savePet(navController: NavController) {


        val newPet = Pet(
            type = petType,
            name = petName,
            age = petAge,
            breed = petBreed,
            image = selectedImageUri?.toString() ?: "",
            id = 0 // El ID se asignará automáticamente en el servidor
        )

        val petService = RetrofitClient.instance.create(PetService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Crear RequestBody para los campos de texto
                val typePart = RequestBody.create("text/plain".toMediaTypeOrNull(), newPet.type)
                val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), newPet.name)
                val agePart = RequestBody.create("text/plain".toMediaTypeOrNull(), newPet.age.toString())
                val breedPart = RequestBody.create("text/plain".toMediaTypeOrNull(), newPet.breed)

                // Crear MultipartBody.Part para el archivo de imagen
                val imageFile = File(getRealPathFromURI(context, selectedImageUri) ?: "")
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

                val response = petService.uploadPet(
                    typePart,
                    namePart,
                    agePart,
                    breedPart,
                    imagePart
                )

                if (response.isSuccessful) {
                    // Mascota almacenada exitosamente
                    val responseBody = response.body()
                    val newPetId = responseBody?.petId
                    Log.d("mascota", "Respuesta: ${newPetId}")


                    withContext(Dispatchers.Main) {
                        navController.navigate(Screen.PetDetails.route + "/$newPetId")
                    }
                } else {
                    Log.d("savePet", "Error en la DB: ${response}")
                    //Log.d("savePet", "Error en la DB: ${newPet}")
                }
            } catch (e: Exception) {
                Log.d("savePet", e.toString())
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(horizontal = 35.dp, vertical = 80.dp),
    ) {
        Column() {
            //title
            textTitle(stringResource(R.string.new_pet_button), false)
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            //pet name
            textContent(stringResource(R.string.pet_name), false)
            customTextField(value = petName, onValueChange = { petName = it })
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            //pet type
            textContent(stringResource(R.string.pet_type), false)
            dropDownMenu(list = typeList,selectedValue = selectedType,onValueChange = { selectedType = it }
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            //pet age
            textContent(stringResource(R.string.pet_age), false)
            customTextField(value = petAge, onValueChange = { petAge = it })
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            // pet breed
            textContent(stringResource(R.string.pet_breed), false)
            customTextField(value = petBreed, onValueChange = { petBreed = it })
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
                ButtonSaveComponent(
                    column = 2,
                    text = stringResource(R.string.save_button),
                    white = true,
                    "",
                    navController = navController
                ){
                    savePet(navController)
                }
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                navButton(
                    column = 2,
                    route = Screen.Home.route,
                    navController = navController,
                    text = stringResource(R.string.cancel_button), white = true
                )
            }
        }
    }
}

