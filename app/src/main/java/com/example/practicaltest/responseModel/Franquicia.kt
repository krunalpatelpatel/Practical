package com.example.practicaltest.responseModel

data class Franquicia(
    val APIKEY: String,
    val franquicia: String,
    val horaCierreLocal: String,
    val id_franquicia: String,
    val negocio: String,
    val principal: Boolean,
    val tokenInvu: String
)