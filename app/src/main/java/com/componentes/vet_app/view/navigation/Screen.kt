package com.componentes.vet_app.view.navigation

sealed class Screen(val route: String){
    object Home: Screen("home_screen")
    object NewPet: Screen("new_pet_screen")
    object PetDetails: Screen("pet_details_screen")

}