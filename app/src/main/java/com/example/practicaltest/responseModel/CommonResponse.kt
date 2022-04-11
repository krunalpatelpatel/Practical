package com.example.practicaltest.responseModel

data class CommonResponse(
    val code: Int,
    val error: Boolean,
    val mensaje: String
)