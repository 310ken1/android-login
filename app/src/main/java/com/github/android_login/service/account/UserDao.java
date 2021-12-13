package com.github.android_login.service.account;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:ids)")
    List<User> loadAllByIds(int[] ids);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}

