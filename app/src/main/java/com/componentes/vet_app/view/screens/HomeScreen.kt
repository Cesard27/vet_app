package com.componentes.vet_app.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.componentes.vet_app.R
import com.componentes.vet_app.model.Pet
import com.componentes.vet_app.model.connect.PetService
import com.componentes.vet_app.model.connect.RetrofitClient
import com.componentes.vet_app.view.navigation.Screen
import com.componentes.vet_app.view.ui.theme.*
import kotlinx.coroutines.launch
import retrofit2.Response
import androidx.compose.foundation.lazy.items

@Composable
fun HomeScreen(navController: NavController) {
    val petService = RetrofitClient.instance.create(PetService::class.java)
    var pets by remember { mutableStateOf(emptyList<Pet>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response: Response<List<Pet>> = petService.getAllPets()
                if (response.isSuccessful) {
                    pets = response.body() ?: emptyList()
                    Log.d("HomeScreen", "Pets loaded successfully: $pets")
                } else {
                    Log.e("HomeScreen", "Error loading pets: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeScreen", "Exception loading pets: ${e.message}")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(horizontal = 35.dp, vertical = 80.dp),
    ) {
        Column {
            navButton(
                column = 1,
                route = Screen.NewPet.route,
                navController = navController,
                text = stringResource(R.string.new_pet_button), white = true)

            Spacer(modifier = Modifier.padding(vertical = 26.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                //textContent("Order by: ", false)
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                //dropDownMenu()
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            searchComponent(navController, pets)
        }
    }
}

@Composable
fun searchComponent(navController: NavController, pets: List<Pet>) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    var searchResults by remember { mutableStateOf(pets) }

    LaunchedEffect(pets) {
        searchResults = pets
    }

    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                searchResults = filterData(pets, it.text)
            },
            shape = RoundedCornerShape(50.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Black)
            },
            label = { textSpecs("Buscar", false) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults) { pet ->
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.PetDetails.route + "/${pet.id}")
                        }
                ) {
                    Column {
                        textSpecs(text = "Nombre: ${pet.name}", false)
                        textSpecs(text = "Raza: ${pet.breed}", false)
                    }
                }
            }
        }
    }
}

fun filterData(data: List<Pet>, query: String): List<Pet> {
    return if (query.isEmpty()) {
        data
    } else {
        data.filter { it.name.contains(query, ignoreCase = true) || it.breed.contains(query, ignoreCase = true) }
    }
}