package com.fredprojects.aidlsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    private final String login;
    private final int id;

    public UserInfo(String login, int id) {
        this.login = login;
        this.id = id;
    }

    private UserInfo(Parcel in) {
        login = in.readString();
        id = in.readInt();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(login);
    }
}
