package com.karimmammadov.currencyrates.service

import com.karimmammadov.currencyrates.model.CurrencyModel
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyAPI {

    @GET("latest?access_key=f4a90b01433fcef9c04042bd70da5f67")
    fun getCurrencies(): Call<CurrencyModel>
}