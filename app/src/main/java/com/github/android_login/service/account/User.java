package com.github.android_login.service.account;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "password")
    public byte[] password;

    @ColumnInfo(name = "authority")
    public int authority;

    public User(int id, byte[] password, int authority) {
        this.id = id;
        this.password = password;
        this.authority = authority;
    }
}
