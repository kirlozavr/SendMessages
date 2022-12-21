package com.example.sendmessages.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.Interface.OnClickListener;
import com.example.sendmessages.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterChats extends RecyclerView.Adapter<RecyclerViewAdapterChats.RecyclerViewHolder> {

    private LayoutInflater inflater;
    private List<ChatsEntity> chatsList = new ArrayList<ChatsEntity>();
    private OnClickListener<ChatsEntity> onClickListener;

    public RecyclerViewAdapterChats(
            Context context,
            OnClickListener<ChatsEntity> onClickListener
    ) {
        this.onClickListener = onClickListener;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<ChatsEntity> chatsList){
        this.chatsList = chatsList;
        notifyDataSetChanged();
    }

    public void deleteList(){
        this.chatsList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerViewHolder holder,
            @SuppressLint("RecyclerView") int position
    ) {
        ChatsEntity chatsEntity = chatsList.get(position);
        holder.textChatsName.setText(chatsEntity.getUsernameToWhom());
        holder.textLastMessages.setText(chatsEntity.getLastMessage());
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(chatsEntity, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView textChatsName;
        private TextView textLastMessages;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textChatsName = itemView.findViewById(R.id.textChatsName);
            textLastMessages = itemView.findViewById(R.id.textLastMessages);
        }
    }

}
