package com.example.logbook3.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.logbook3.Database.AppDatabase;
import com.example.logbook3.Models.User;
import com.example.logbook3.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements ContactAdapter.OnDeleteClickListener {
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private FloatingActionButton floatingAdd;
    List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        users = appDatabase.userDao().getAllUsers();
        adapter = new ContactAdapter(users,this);
        recyclerView.setAdapter(adapter);

        floatingAdd = findViewById(R.id.floatingActionButton2);
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Handle the FloatingActionButton Click event here
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onDeleteClick(User user) {
        // Delete the user from the database
        appDatabase.userDao().deleteUser(user);

        // Update the RecyclerView after deletion
        users.remove(user);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
    }

}