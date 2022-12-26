package com.example.sendmessages.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.DTO.ChatsDto;
import com.example.sendmessages.Interface.OnClickListener;
import com.example.sendmessages.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterChats extends RecyclerView.Adapter<RecyclerViewAdapterChats.RecyclerViewHolder> {

    private LayoutInflater inflater;
    private List<ChatsDto> chatsList = new ArrayList<ChatsDto>();
    private OnClickListener<ChatsDto> onClickListener;

    public RecyclerViewAdapterChats(
            Context context,
            OnClickListener<ChatsDto> onClickListener
    ) {
        this.onClickListener = onClickListener;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<ChatsDto> chatsList){
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
        ChatsDto chatsDto = chatsList.get(position);
        holder.textChatsName.setText(chatsDto.getUsernameToWhom());
        holder.textLastMessages.setText(chatsDto.getLastMessage());
        holder.timeMessage.setText(chatsDto.getTimeMessageToChats());
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(chatsDto, position);
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
        private TextView timeMessage;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textChatsName = itemView.findViewById(R.id.textChatsName);
            textLastMessages = itemView.findViewById(R.id.textLastMessages);
            timeMessage = itemView.findViewById(R.id.timeMessage);
        }
    }

}
