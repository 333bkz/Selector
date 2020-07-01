package com.example.myapplication.selectPop;

import java.util.List;

public interface EditSelectDataListener<T extends Select> {
    void edit(List<T> selects);
}
