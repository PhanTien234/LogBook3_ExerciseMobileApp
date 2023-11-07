package com.example.logbook3.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logbook3.Models.User;
import com.example.logbook3.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<User> users;
    private OnDeleteClickListener onDeleteClickListener;
    public interface OnDeleteClickListener {
        void onDeleteClick(User person);
    }
    public ContactAdapter(List<User> users, OnDeleteClickListener onDeleteClickListener) {
        this.users = users;
        this.onDeleteClickListener = onDeleteClickListener;
    }
    public ContactAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_card, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        User user = users.get(position);
        holder.userName.setText(user.name);
        holder.userDob.setText(user.dob);
        holder.userEmail.setText(user.email);
        holder.userImage.setImageResource(user.image);
        holder.itemView.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(users.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userDob, userEmail;
        ImageView userImage;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userDob = itemView.findViewById(R.id.userDob);
            userEmail = itemView.findViewById(R.id.userEmail);
            userImage = itemView.findViewById(R.id.userImage);
        }
    }
}
