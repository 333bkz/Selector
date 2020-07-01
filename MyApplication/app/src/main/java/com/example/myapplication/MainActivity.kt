package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.selectPop.DataChangeListener
import com.example.myapplication.selectPop.EditSelectDataListener
import com.example.myapplication.selectPop.SelectHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), DataChangeListener<Data>, EditSelectDataListener<Data> {

    private lateinit var handler: SelectHandler<Data>
    private val data: ArrayList<Data> = ArrayList()
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = Adapter(data, this, this)
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter
        handler = SelectHandler.instance(this, adapter, this)
    }

    fun showDialog(view: View) {
        handler.showDialog()
    }

    fun load(view: View) {
        GlobalScope.launch(Dispatchers.Default) {
            for (i in 0..5) {
                data.add(Data(i.toString()))
            }
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
                handler.onChanged(-1)
            }
        }

    }

    override fun change(index: Int, item: Data) {
        handler.onChanged(index)
    }

    override fun edit(selects: List<Data>) {
        data.removeAll(selects)
        handler.showDialog()
    }
}