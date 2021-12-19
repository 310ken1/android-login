package com.github.android_login.service.account;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE id=(:id)")
    User get(String id);

    @Query("SELECT * FROM users WHERE id IN (:ids)")
    List<User> get(String[] ids);

    @Query("SELECT * FROM users")
    List<User> get();

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
