package com.example.logbook3.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.logbook3.Database.AppDatabase;
import com.example.logbook3.Models.User;
import com.example.logbook3.R;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
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
        adapter = new ContactAdapter(users);
        recyclerView.setAdapter(adapter);
        Button viewDetailsButton = findViewById(R.id.backButton);
        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }
    private  void back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}