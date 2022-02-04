package com.karimmammadov.currencyrates.service

import com.karimmammadov.currencyrates.model.CurrencyModel
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyAPI {

    @GET("latest?access_key=d214486cf3fe3c6e17287f6a1f6b9f03")
    fun getCurrencies(): Call<CurrencyModel>
}