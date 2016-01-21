package com.clover_studio.spikachatmodule.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.clover_studio.spikachatmodule.base.BaseModel;

/**
 * Created by ubuntu_ivo on 21.07.15..
 */
public class User extends BaseModel implements Parcelable{

    public String userID;
    public String name;
    public String avatarURL;
    public String roomID;
    public String pushToken;

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", roomID='" + roomID + '\'' +
                ", pushToken='" + pushToken + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userID != null ? !userID.equals(user.userID) : user.userID != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        return !(name != null ? !name.equals(user.name) : user.name != null);

    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (avatarURL != null ? avatarURL.hashCode() : 0);
        result = 31 * result + (roomID != null ? roomID.hashCode() : 0);
        result = 31 * result + (pushToken != null ? pushToken.hashCode() : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userID);
        dest.writeString(this.name);
        dest.writeString(this.avatarURL);
        dest.writeString(this.roomID);
        dest.writeString(this.pushToken);
    }

    public User() {
    }

    private User(Parcel in) {
        this.userID = in.readString();
        this.name = in.readString();
        this.avatarURL = in.readString();
        this.roomID = in.readString();
        this.pushToken = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
