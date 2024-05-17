package com.componentes.vet_app.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.componentes.vet_app.view.screens.HomeScreen
import com.componentes.vet_app.view.screens.NewPetScreen
import com.componentes.vet_app.view.screens.PetDetailsScreen


@Composable
fun SetupNavGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(
            route = Screen.Home.route
        ){
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.NewPet.route
        ){
            NewPetScreen(navController = navController)
        }
        composable(
            route = Screen.PetDetails.route
        ){
            PetDetailsScreen(navController = navController)
        }

    }
}