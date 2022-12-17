package com.example.sendmessages.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Interface.OnClickListener;
import com.example.sendmessages.Items.RecycleViewItemSearch;
import com.example.sendmessages.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapterSearch extends RecyclerView.Adapter<RecycleViewAdapterSearch.RecycleHolderSearch> {

    private final OnClickListener<RecycleViewItemSearch> onClickListener;
    private List<RecycleViewItemSearch> searchList = new ArrayList<RecycleViewItemSearch>();
    private LayoutInflater layoutInflater;

    public RecycleViewAdapterSearch(
            Context context,
            OnClickListener<RecycleViewItemSearch> onClickListener) {
        this.onClickListener = onClickListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setSearchList(List<RecycleViewItemSearch> searchList) {
        this.searchList = searchList;
        notifyDataSetChanged();
    }

    public void deleteList(){
        this.searchList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleViewAdapterSearch.RecycleHolderSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);
        return new RecycleHolderSearch(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecycleViewAdapterSearch.RecycleHolderSearch holder,
            @SuppressLint("RecyclerView") int position
    ) {
        RecycleViewItemSearch recycleViewItemSearch = searchList.get(position);
        holder.textViewSearch.setText(recycleViewItemSearch.getUsername());
        holder.userId = recycleViewItemSearch.getUserId();
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickListener.onClick(recycleViewItemSearch, position);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class RecycleHolderSearch extends RecyclerView.ViewHolder {

        private TextView textViewSearch;
        private int userId;

        public RecycleHolderSearch(@NonNull View itemView) {
            super(itemView);

            textViewSearch = itemView.findViewById(R.id.textViewSearch);
        }
    }
}
