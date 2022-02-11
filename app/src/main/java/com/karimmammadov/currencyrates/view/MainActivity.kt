package com.karimmammadov.currencyrates.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karimmammadov.currencyrates.R
import com.karimmammadov.currencyrates.adapter.RecyclerViewAdapter
import com.karimmammadov.currencyrates.model.CurrencyModel
import com.karimmammadov.currencyrates.service.CurrencyAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_layout.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
    }
    private lateinit var currencyList : ArrayList<CurrencyModel>
    private lateinit var currencyListNew : ArrayList<CurrencyModel>
    private val BASE_URL = "http://data.fixer.io/api/"
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //f4a90b01433fcef9c04042bd70da5f67
        //http://data.fixer.io/api/latest?access_key=f4a90b01433fcef9c04042bd70da5f67
        //RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        mainProgressBar.visibility = View.VISIBLE
        loadData()

    }

    private fun loadData() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(CurrencyAPI::class.java)
        val call = service.getCurrencies()

        call.enqueue(object : Callback<CurrencyModel> {
            override fun onResponse(
                call: Call<CurrencyModel>,
                response: Response<CurrencyModel>
            ) {
                if (response.isSuccessful) {
                    handleResponse(response)
                }
            }

            override fun onFailure(call: Call<CurrencyModel>, t: Throwable) {
                Log.e(LOG_TAG, "onFailure", t)
            }
        })
    }

    private fun handleResponse(response: Response<CurrencyModel>) {
        mainProgressBar.visibility = View.GONE

        response.body()?.let {

            baseTextView.text = String.format(resources.getString(R.string.baseLabel), it.base)
            dateTextView.text = String.format(resources.getString(R.string.dateLabel), it.date)

            recyclerViewAdapter =
                RecyclerViewAdapter(it.rates.toList(), this@MainActivity)
            recyclerView.adapter = recyclerViewAdapter

        }
    }

    override fun onItemClick(currencyModel: Pair<String, Double>) {
        Toast.makeText(this, "Clicked : ${currencyModel.first}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var item: MenuItem = menu!!.findItem(R.id.menu_search)
        if (item != null){
            var searchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    currencyList = ArrayList()
                 if(newText!!.isNotEmpty()){
                     currencyList.clear()
                     var search = newText.toLowerCase(Locale.getDefault())
                     for (currency in currencyList){
                        if (currency.toString().toLowerCase(Locale.getDefault()).contains(search)){
                            currencyListNew.add(currency)
                        }
                         recyclerViewAdapter!!.notifyDataSetChanged()
                     }
                 }else{
                     currencyListNew.clear()
                     currencyListNew.addAll(currencyList)
                     recyclerViewAdapter!!.notifyDataSetChanged()
                 }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }
}
