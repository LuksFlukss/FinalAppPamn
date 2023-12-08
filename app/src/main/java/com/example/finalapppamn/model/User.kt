package com.example.finalapppamn.model

data class User(
    var name: String? = "",
    var email: String? = "",
    var aboutYou: String? = "",
    var favoritePublicationIds: MutableList<String> = mutableListOf()
) {

    // Métodos para agregar y eliminar publicaciones favoritas

    fun addFavoritePublicationId(publicationId: String): Boolean {
        return favoritePublicationIds.add(publicationId)
    }

    fun removeFavoritePublicationId(publicationId: String): Boolean {
        return favoritePublicationIds.remove(publicationId)
    }
}
