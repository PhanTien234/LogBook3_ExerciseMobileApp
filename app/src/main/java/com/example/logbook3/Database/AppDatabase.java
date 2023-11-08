package com.example.logbook3.Database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.logbook3.Dao.UserDao;
import com.example.logbook3.Models.User;

@Database(entities = {User.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
