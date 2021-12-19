package com.github.android_login.service.account;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "password")
    public byte[] password;

    @ColumnInfo(name = "authority")
    public int authority;

    public User(@NonNull String id, @NonNull byte[] password, int authority) {
        this.id = id;
        this.password = password;
        this.authority = authority;
    }
}
