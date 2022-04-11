package com.example.practicaltest.utils

import com.example.practicaltest.responseModel.CommonResponse
import com.google.gson.GsonBuilder
import retrofit2.Response
import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


sealed class Result<T>(val code: Int = 200, val responseData: T? = null, val msg: String? = null) {

    class Loading<T> : Result<T>()

    class Success<T>(code: Int, data: T?) : Result<T>(code, data)

    class Error<T>(code: Int, msg: String?) : Result<T>(code = code, msg = msg)
}

suspend fun <T> safeAPIcall(apicall: suspend () -> Response<T>): Result<T> {
    try {
        return apicall.invoke().toResult()
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is ProtocolException) {
            return try {
                apicall.invoke().toResult()
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    e.printStackTrace()
                    Result.Error(1001, "you are offline")
                } else {
                    e.printStackTrace()
                    Result.Error(1002, "Something went wrong")
                }
            }
        } else if (e is SocketTimeoutException) {
            e.printStackTrace()
            return Result.Error(1001, "you are offline")
        } else if (e is UnknownHostException) {
            e.printStackTrace()
            return Result.Error(1001, "you are offline")
        } else {
            e.printStackTrace()
            return Result.Error(1002, "Something went wrong")
        }

    }
}

fun <T> Response<T>.toResult(): Result<T> {
    if (isSuccessful) {
        return Result.Success(code(), data = body())
    } else {
        return try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val response: CommonResponse =
                gson.fromJson(errorBody()?.string(), CommonResponse::class.java)
            Result.Error(code(), response.mensaje)
        } catch (e: Exception) {
            Result.Error(code(), null)
        }
    }
}
