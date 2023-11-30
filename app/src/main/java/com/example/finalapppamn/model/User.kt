package com.example.finalapppamn.model

class User(
    val name: String,
    val password: String,
    val email: String,
    val favoritos: Array<Int> //Formado por las id de las publicaciones
)