package com.example.sendmessages.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.DTO.MessageDto;
import com.example.sendmessages.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterMessages extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageDto> messageList = new ArrayList<MessageDto>();
    private LayoutInflater layoutInflater;
    private final String usernameFrom;
    private final int TYPE_FROM = 0;
    private final int TYPE_TO_WHOM = 1;

    public RecyclerViewAdapterMessages(Context context, String usernameFrom){
        this.usernameFrom = usernameFrom;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<MessageDto> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public void deleteList(){
        this.messageList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        MessageDto message = messageList.get(position);
        if(message.getUsernameFrom().equals(usernameFrom)){
            return TYPE_FROM;
        } else {
            return TYPE_TO_WHOM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_FROM){
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_message_from, parent, false);
            return new RecycleHolderMessageFrom(view);
        } else {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_message_to_whom, parent, false);
            return new RecycleHolderMessageToWhom(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            @SuppressLint("RecyclerView") int position
    ) {
        if(holder.getItemViewType() == TYPE_FROM){
            messageFrom(
                    (RecycleHolderMessageFrom) holder,
                    position
            );
        } else {
            messageToWhom(
                    (RecycleHolderMessageToWhom) holder,
                    position
            );
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private void messageFrom(
            @NonNull RecycleHolderMessageFrom holder,
            int position
    ){
        MessageDto message = messageList.get(position);
        holder.textFrom
                .setText(message.getMessage());
        holder.timeFrom
                .setText(message.getDateTimeToMessages()
                );
    }

    private void messageToWhom(@NonNull RecycleHolderMessageToWhom holder,
                               int position
    ){
        MessageDto message = messageList.get(position);
        holder.textToWhom
                .setText(message.getMessage());
        holder.timeToWhom
                .setText(message.getDateTimeToMessages()
                );
    }

    public class RecycleHolderMessageFrom extends RecyclerView.ViewHolder {

        private TextView textFrom;
        private TextView timeFrom;

        public RecycleHolderMessageFrom(@NonNull View itemView) {
            super(itemView);
            textFrom = itemView.findViewById(R.id.textFrom);
            timeFrom = itemView.findViewById(R.id.timeFrom);
        }
    }

    public class RecycleHolderMessageToWhom extends RecyclerView.ViewHolder {

        private TextView textToWhom;
        private TextView timeToWhom;

        public RecycleHolderMessageToWhom(@NonNull View itemView) {
            super(itemView);
            textToWhom = itemView.findViewById(R.id.textToWhom);
            timeToWhom = itemView.findViewById(R.id.timeToWhom);
        }
    }
}
