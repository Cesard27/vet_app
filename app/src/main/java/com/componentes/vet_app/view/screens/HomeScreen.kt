package com.componentes.vet_app.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.componentes.vet_app.R
import com.componentes.vet_app.view.navigation.Screen
import com.componentes.vet_app.view.ui.theme.*

@Composable
fun HomeScreen(navController: NavController){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(horizontal = 35.dp, vertical = 80.dp),
    ){
        Column() {
            navButton(
                column = 1,
                route = Screen.NewPet.route,
                navController = navController,
                text = stringResource(R.string.new_pet_button), white = true)

            Spacer(modifier = Modifier.padding(vertical = 26.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                textContent("Order by: ", false)
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                dropDownMenu()
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            searchComponent(navController, Screen.PetDetails)

        }


    }

}






@Composable
fun searchComponent(navController: NavController, route: Screen.PetDetails){
    val context = LocalContext.current
    val data = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit){
        // space to show info of database
    }

    SearchableTextField(data) {

    }

}

@Composable
fun SearchableTextField(data: List<String>, onItemClick: (String) -> Unit){
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    var searchResults by remember { mutableStateOf(data) }

    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                searchResults = filterData(data, it.text)
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
            items(searchResults.size) { item ->
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick("$item") }
                    ){
                        textSpecs(
                            text = "$item",
                            false
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(Unit){
        onDispose {  }
    }
}

fun filterData(data: List<String>, query: String): List<String>{
    return if (query.isEmpty()){
        data
    } else {
        data.filter { it.contains(query, ignoreCase = true) }
    }
}

