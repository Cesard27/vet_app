package com.componentes.vet_app.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.componentes.vet_app.R
import com.componentes.vet_app.view.navigation.Screen
import com.componentes.vet_app.view.ui.theme.*

@Composable
fun PetDetailsScreen(navController: NavController){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(horizontal = 35.dp, vertical = 80.dp),
    ){
        Column() {

            // Provisional code
            textTitle("Pet name", false)

            Spacer(modifier = Modifier.padding(vertical = 26.dp))

            textContent("${LoremIpsum(50)}", false)

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Image(
                painter = painterResource(id = R.drawable.placeholder),
                modifier = Modifier.size(width = 500.dp, height = 300.dp),
                contentDescription = "placeHolderImage")


            Spacer(modifier = Modifier.padding(vertical = 28.dp))

            navButton(
                navController = navController,
                text = stringResource(R.string.go_to_list_button),
                white = true,
                route = Screen.Home.route,
                column = 1)

        }
    }

}