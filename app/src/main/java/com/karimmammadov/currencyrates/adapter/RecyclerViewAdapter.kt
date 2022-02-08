package com.karimmammadov.currencyrates.adapter

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.karimmammadov.currencyrates.R
import com.karimmammadov.currencyrates.model.CurrencyModel
import kotlinx.android.synthetic.main.row_layout.view.*
import java.util.*
import java.util.logging.Filter
import java.util.logging.LogRecord
import kotlin.collections.ArrayList


class RecyclerViewAdapter(
    private var currencyList: List<Pair<String, Double>>,
    private val listener: Listener
) : RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>(), Filterable {
    lateinit var itemModelListFilter: ArrayList<CurrencyModel>
    lateinit var itemModel: ArrayList<CurrencyModel>

    interface Listener {
        fun onItemClick(currencyModel: Pair<String, Double>)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(currencyModel: Pair<String, Double>, listener: Listener) {
            itemView.setOnClickListener {
                listener.onItemClick(currencyModel)
            }
            itemView.currencyName.text = currencyModel.first
            itemView.currencyValue.text =
                (Math.round(currencyModel.second * 100.0) / 100.0).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(currencyList[position], listener)
    }

    override fun getItemCount(): Int {
        return currencyList.count()
    }

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                var filterResults = FilterResults()
                if (charSequence == null || charSequence.isEmpty()) {
                    filterResults.count = itemModelListFilter.size
                    filterResults.values = itemModelListFilter
                } else {
                    var searchChr: String = charSequence.toString().toLowerCase()
                    for (items in itemModelListFilter) {
                        if (items.rates.toString().contains(searchChr) || items.base.toLowerCase()
                                .contains(searchChr)
                        ) {
                            itemModel.add(items)
                        }
                    }
                    filterResults.count = itemModel.size
                    filterResults.values = itemModel
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

                 arrayOf(currencyList) == p1!!.values as ArrayList<CurrencyModel>
                notifyDataSetChanged()

            }
        }
    }
}


