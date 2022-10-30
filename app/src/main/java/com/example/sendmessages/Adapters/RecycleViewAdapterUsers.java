package com.example.sendmessages.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.R;
import com.example.sendmessages.Items.RecycleViewItemUsers;

import java.util.List;

public class RecycleViewAdapterUsers extends RecyclerView.Adapter<RecycleViewAdapterUsers.RecyclerViewHolder> {

    private List<RecycleViewItemUsers> usersList;
    private LayoutInflater inflater;

    public RecycleViewAdapterUsers(Context context, List<RecycleViewItemUsers> usersList){
        this.usersList = usersList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_messages_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecycleViewItemUsers recycleViewItemUsers = usersList.get(position);
        holder.textViewNameMessages.setText(recycleViewItemUsers.getUsernameSend());
        holder.textViewMessages.setText(recycleViewItemUsers.getLastText());
        holder.username_this = recycleViewItemUsers.getUsernameThis();
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;
        private TextView textViewNameMessages;
        private TextView textViewMessages;
        private String username_this;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayoutItemMessages);
            textViewNameMessages = itemView.findViewById(R.id.textViewNameMessages);
            textViewMessages = itemView.findViewById(R.id.textViewMessages);
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }

        public TextView getTextViewNameMessages() {
            return textViewNameMessages;
        }

        public TextView getTextViewMessages() {
            return textViewMessages;
        }

        public String getUsernameThis() {
            return username_this;
        }
    }

}
