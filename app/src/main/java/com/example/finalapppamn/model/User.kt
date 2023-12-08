package com.example.finalapppamn.model

data class User(
    var name: String? = "",
    var email: String? = "",
    var aboutYou: String? = "",
    var favoritePublicationIds: MutableList<String> = mutableListOf()
) {



    fun setname(name: String){
        this.name = name
    }


    fun setemail(email: String){
        this.email = email
    }


    fun setaboutYou(aboutYou: String){
        this.aboutYou = aboutYou
    }

    fun getfavoritePublicationIds():MutableList<String>{
        return favoritePublicationIds
    }

    fun setfavoritePublicationIds(favoritePublicationIds: MutableList<String>){
        this.favoritePublicationIds = favoritePublicationIds
    }

    // Métodos para agregar y eliminar publicaciones favoritas

    fun addFavoritePublicationId(publicationId: String): Boolean {
        return favoritePublicationIds.add(publicationId)
    }

    fun removeFavoritePublicationId(publicationId: String): Boolean {
        return favoritePublicationIds.remove(publicationId)
    }
}
