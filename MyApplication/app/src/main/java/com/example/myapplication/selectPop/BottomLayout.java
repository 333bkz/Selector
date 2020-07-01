package com.example.myapplication.selectPop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;

public class BottomLayout extends LinearLayout implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CheckBox checkbox;
    private TextView action, count;
    private View rlAction;
    private StateChangeListener listener;
    private View contentView;

    private boolean isShowing;

    public BottomLayout(@NonNull Context context) {
        super(context);
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet, this, true);
        checkbox = contentView.findViewById(R.id.checkbox);
        action = contentView.findViewById(R.id.action);
        count = contentView.findViewById(R.id.count);
        rlAction = contentView.findViewById(R.id.rl_action);
        checkbox.setOnCheckedChangeListener(this);
        rlAction.setOnClickListener(this);
        setEnable(false);
        setVisibility(GONE);
        isShowing = false;
    }

    public void setStateChangeListener(StateChangeListener listener) {
        this.listener = listener;
    }

    public void setAction(String action) {
        this.action.setText(action);
    }

    public void setCount(int count) {
        if (count > 0) {
            this.count.setVisibility(View.VISIBLE);
            this.count.setText(String.valueOf(count));
        } else {
            this.count.setVisibility(View.GONE);
        }
    }

    public void selectAll(boolean isSelectAll) {
        checkbox.setChecked(isSelectAll);
    }


    public void show() {
        reset();
        if (isShowing) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
        isShowing = !isShowing;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setEnable(boolean enable) {
        checkbox.setEnabled(enable);
        rlAction.setEnabled(enable);
    }

    public void reset() {
        count.setText("");
        count.setVisibility(View.GONE);
        checkbox.setChecked(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.isPressed()) {
            if (listener != null)
                listener.selectAll(b);
        }
    }

    @Override
    public void onClick(View view) {
        if (listener != null) listener.action();
    }

    public View getContentView() {
        return contentView;
    }
}
