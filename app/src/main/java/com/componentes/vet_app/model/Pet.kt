package com.componentes.vet_app.model

data class Pet(
    val id: Int,
    var type: String,
    val name: String,
    val age: String,
    val breed: String,
    val image: String
)