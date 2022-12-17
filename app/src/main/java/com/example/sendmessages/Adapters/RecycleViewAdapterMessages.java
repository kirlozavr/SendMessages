package com.example.sendmessages.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Entity.MessageEntity;
import com.example.sendmessages.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapterMessages extends RecyclerView.Adapter<RecycleViewAdapterMessages.RecycleHolderMessage> {

    private List<MessageEntity> messageList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public void RecycleViewAdapterMessages(Context context){
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<MessageEntity> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public void deleteList(){
        this.messageList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleViewAdapterMessages.RecycleHolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.message_from, parent, false);
        return new RecycleHolderMessage(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecycleViewAdapterMessages.RecycleHolderMessage holder,
            int position
    ) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy  hh:mm");
        MessageEntity messageEntity = messageList.get(position);
        holder.textFrom.setText(messageEntity.getMessage());
        holder.timeTextFrom.setText(
                simpleDateFormat
                        .format(messageEntity.getDateTime())
        );
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class RecycleHolderMessage extends RecyclerView.ViewHolder {

        private TextView textFrom;
        private TextView timeTextFrom;
        private String usernameFrom, usernameToWhom;

        public RecycleHolderMessage(@NonNull View itemView) {
            super(itemView);

            textFrom = itemView.findViewById(R.id.textFrom);
            timeTextFrom = itemView.findViewById(R.id.timeTextFrom);

        }
    }
}
