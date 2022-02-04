package com.karimmammadov.currencyrates.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karimmammadov.currencyrates.R
import kotlinx.android.synthetic.main.row_layout.view.*

class RecyclerViewAdapter(
    private val currencyList: List<Pair<String, Double>>,
    private val listener: Listener) : RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {
    interface Listener {
        fun onItemClick(currencyModel: Pair<String, Double>)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(currencyModel: Pair<String, Double>, listener: Listener) {
            itemView.setOnClickListener {
                listener.onItemClick(currencyModel)
            }
            itemView.currencyName.text = currencyModel.first
            itemView.currencyValue.text = currencyModel.second.toString()
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
}