package com.example.datastorage.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserData {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "user_input")
    public String userInput;

    public UserData(String userInput) {
        this.userInput = userInput;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }
}
