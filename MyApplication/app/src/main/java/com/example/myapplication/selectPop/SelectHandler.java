package com.example.myapplication.selectPop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SelectHandler<T extends Select> implements BottomSheet.StateChangeListener {
    private BaseAdapter<T, ?> adapter;
    private BottomSheet dialog;
    private RecyclerView recyclerView;
    private View placeView;
    private EditSelectDataListener<T> listener;

    public interface EditSelectDataListener<T> {
        void edit(List<T> selects);
    }

    private SelectHandler() {
    }

    public static <T extends Select> SelectHandler<T> instance(Context context,
                                                               BaseAdapter<T, ?> adapter,
                                                               RecyclerView recyclerView,
                                                               View placeView,
                                                               EditSelectDataListener<T> listener) {
        SelectHandler<T> instance = new SelectHandler<>();
        instance.placeView = placeView;
        instance.recyclerView = recyclerView;
        instance.adapter = adapter;
        instance.listener = listener;
        instance.dialog = new BottomSheet(context);
        instance.dialog.setStateChangeListener(instance);
        return instance;
    }

    public void showDialog() {
        for (T item : adapter.getData()) {
            item.setSelect(false);
        }
        dialog.show();
        adapter.notifyShowSelectChanged(dialog.isShowing());
        dialog.setEnable(!adapter.getData().isEmpty());

        if (placeView.getHeight() == 0) {
            dialog.getContentView().measure(0, 0);
            final int h = dialog.getContentView().getMeasuredHeight();
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
            placeView.setLayoutParams(params);
        }

        if (dialog.isShowing()) {
            placeView.setVisibility(View.VISIBLE);
            final boolean isVisBottom = isVisBottom(recyclerView);
            if (isVisBottom)
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        } else {
            placeView.setVisibility(View.GONE);
        }
    }

    public void onChanged(int index) {
        if (index < 0) {
            dialog.setEnable(!adapter.getData().isEmpty());
            return;
        }
        adapter.notifyItemChanged(index);
        if (dialog.isShowing()) {
            final List<T> data = adapter.getData();
            int count = 0;
            if (data.isEmpty()) {
                dialog.setEnable(false);
            } else {
                dialog.setEnable(true);
                for (T bean : data) {
                    if (bean.isSelect()) count++;
                }
            }
            dialog.setCount(count);
            final boolean isSelectAll = count != 0 && count == data.size();
            dialog.selectAll(isSelectAll);
        }
    }

    @Override
    public void selectAll(boolean isSelectAll) {
        for (T item : adapter.getData()) {
            item.setSelect(isSelectAll);
        }
        adapter.notifyDataSetChanged();
        dialog.setCount(isSelectAll ? adapter.getData().size() : 0);
    }

    @Override
    public void action() {
        if (listener != null) {
            List<T> select = null;
            for (T item : adapter.getData()) {
                if (item.isSelect()) {
                    if (select == null)
                        select = new ArrayList<>();
                    select.add(item);
                }
            }
            if (select != null)
                listener.edit(select);
        }
    }

    public boolean isVisBottom(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int state = recyclerView.getScrollState();
        return visibleItemCount > 0
                && lastVisibleItemPosition == totalItemCount - 1
                && state == RecyclerView.SCROLL_STATE_IDLE;
    }
}
