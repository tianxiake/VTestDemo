package com.snail.vds;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yongjie created on 2019-09-05.
 */
public class CustomViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> sparseArray = new SparseArray<>();

    public CustomViewHolder(View itemView) {
        super(itemView);
    }

    public View getView(@IdRes int resId) {
        View view = sparseArray.get(resId);
        if (view == null) {
            view = itemView.findViewById(resId);
            sparseArray.put(resId, view);
        }
        return view;
    }
}
