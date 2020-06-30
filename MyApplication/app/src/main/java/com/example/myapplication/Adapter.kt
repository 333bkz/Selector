package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.selectPop.BaseAdapter
import com.example.myapplication.selectPop.DataChangeListener

class Adapter(data: List<Data>, context: Context, listener: DataChangeListener<Data>) :
    BaseAdapter<Data, Adapter.Holder>(data, context, listener) {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item: View = itemView.findViewById(R.id.item)
        var value: TextView = itemView.findViewById(R.id.value)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data[position]
        holder.value.text = item.name
        if (showSelect) {
            holder.checkBox.visibility = View.VISIBLE
            holder.checkBox.isChecked = item.isSelect
            holder.item.setOnClickListener {
                item.isSelect = !item.isSelect
                listener.change(position, item)
            }
        } else {
            holder.checkBox.visibility = View.GONE
            holder.item.setOnClickListener(null)
        }
    }
}

