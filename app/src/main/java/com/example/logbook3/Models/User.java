package com.example.logbook3.Models;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public Long user_id;
    public String name;
    public String dob;
    public String email;
    public Integer image;
}
