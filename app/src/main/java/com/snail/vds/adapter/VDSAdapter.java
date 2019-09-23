package com.snail.vds.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.snail.vds.CustomViewHolder;
import com.snail.vds.R;
import com.snail.vds.entity.ClearWebEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yongjie created on 2019-09-05.
 */
public class VDSAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private List<ClearWebEntity> list = new ArrayList<>();

    public VDSAdapter() {
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new CustomViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ClearWebEntity clearWebEntity = list.get(position);
        TextView textView = (TextView) holder.getView(R.id.tv_index);
        textView.setText(position + 1 + "");
        if (clearWebEntity.isSpecial()) {
            holder.itemView.setBackgroundColor(Color.RED);
        } else if (clearWebEntity.isError()) {
            holder.itemView.setBackgroundColor(Color.YELLOW);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        TextView webText = (TextView) holder.getView(R.id.tv_web);
        webText.setText(clearWebEntity.getWebUrl());
        TextView subText = (TextView) holder.getView(R.id.tv_sub_number);
        subText.setText(clearWebEntity.getSubVid() + "");
        TextView message = (TextView) holder.getView(R.id.tv_message);
        message.setText(clearWebEntity.getMessage());
    }

    public void resetAllData(List<ClearWebEntity> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
