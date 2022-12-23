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
import com.example.sendmessages.DTO.SearchDto;
import com.example.sendmessages.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterSearch extends RecyclerView.Adapter<RecyclerViewAdapterSearch.RecycleHolderSearch> {

    private final OnClickListener<SearchDto> onClickListener;
    private List<SearchDto> searchList = new ArrayList<SearchDto>();
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapterSearch(
            Context context,
            OnClickListener<SearchDto> onClickListener
    ) {
        this.onClickListener = onClickListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<SearchDto> searchList) {
        this.searchList = searchList;
        notifyDataSetChanged();
    }

    public void deleteList(){
        this.searchList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterSearch.RecycleHolderSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);
        return new RecycleHolderSearch(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerViewAdapterSearch.RecycleHolderSearch holder,
            @SuppressLint("RecyclerView") int position
    ) {
        SearchDto searchDto = searchList.get(position);
        holder.textViewSearch.setText(searchDto.getUsername());
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickListener.onClick(searchDto, position);
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

        public RecycleHolderSearch(@NonNull View itemView) {
            super(itemView);

            textViewSearch = itemView.findViewById(R.id.textViewSearch);
        }
    }
}
