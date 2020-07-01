package com.example.myapplication.selectPop;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class SelectHandler<T extends Select> implements StateChangeListener {
    private BaseAdapter<T, ?> adapter;
    private BottomLayout dialog;
    private EditSelectDataListener<T> listener;

    private SelectHandler() {
    }

    public static <T extends Select> SelectHandler<T> instance(Activity activity,
                                                               BaseAdapter<T, ?> adapter,
                                                               EditSelectDataListener<T> listener) {
        SelectHandler<T> instance = new SelectHandler<>();
        instance.adapter = adapter;
        instance.listener = listener;
        instance.dialog = new BottomLayout(activity);
        instance.dialog.setStateChangeListener(instance);
        //使用LinearLayout替换根布局 再添加到该LinearLayout中
        View decorView = activity.getWindow().getDecorView();
        FrameLayout contentParent = decorView.findViewById(android.R.id.content);
        View beforeContent = contentParent.getChildAt(0);
        contentParent.removeView(beforeContent);
        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        contentParent.addView(content);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                0, 1f);
        beforeContent.setLayoutParams(layoutParams);
        content.addView(beforeContent);
        content.addView(instance.dialog);
        return instance;
    }

    public void showDialog() {
        for (T item : adapter.getData()) {
            item.setSelect(false);
        }
        dialog.show();
        adapter.notifyShowSelectChanged(dialog.isShowing());
        dialog.setEnable(!adapter.getData().isEmpty());
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
}
