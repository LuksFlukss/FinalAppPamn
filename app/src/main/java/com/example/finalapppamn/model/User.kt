package com.example.finalapppamn.model

class User(
    val name: String,
    val email: String,
    val aboutYou: String,
    val favoritePublicationIds: MutableList<String> = mutableListOf()
) {

    // Métodos para agregar y eliminar publicaciones favoritas

    fun addFavoritePublicationId(publicationId: String) {
        favoritePublicationIds.add(publicationId)
    }

    fun removeFavoritePublicationId(publicationId: String) {
        favoritePublicationIds.remove(publicationId)
    }
}