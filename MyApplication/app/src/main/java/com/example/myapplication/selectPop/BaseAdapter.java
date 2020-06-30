package com.example.myapplication.selectPop;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<T extends Select, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> data;
    protected Context context;
    protected DataChangeListener<T> listener;
    protected Boolean showSelect = false;

    public BaseAdapter(List<T> data, Context context, DataChangeListener<T> listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    public List<T> getData() {
        return data;
    }

    public void notifyShowSelectChanged(boolean showSelect) {
        if (showSelect != this.showSelect) {
            this.showSelect = showSelect;
            notifyDataSetChanged();
        }
    }
}
