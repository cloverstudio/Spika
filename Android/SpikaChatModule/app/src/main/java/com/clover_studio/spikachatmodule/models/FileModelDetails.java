package com.clover_studio.spikachatmodule.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */
public class FileModelDetails implements Parcelable{

    public String id;
    public String name;
    public String size;
    public String mimeType;

    public FileModelDetails() {
    }

    @Override
    public String toString() {
        return "UploadFileModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    protected FileModelDetails(Parcel in) {
        id = in.readString();
        name = in.readString();
        size = in.readString();
        mimeType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(mimeType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FileModelDetails> CREATOR = new Parcelable.Creator<FileModelDetails>() {
        @Override
        public FileModelDetails createFromParcel(Parcel in) {
            return new FileModelDetails(in);
        }

        @Override
        public FileModelDetails[] newArray(int size) {
            return new FileModelDetails[size];
        }
    };
}
