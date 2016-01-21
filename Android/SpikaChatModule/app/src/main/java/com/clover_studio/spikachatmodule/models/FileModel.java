package com.clover_studio.spikachatmodule.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileModel implements Parcelable{

    public FileModelDetails file;
    public FileModelDetails thumb;

    public FileModel() {
    }

    @Override
    public String toString() {
        return "UploadFile{" +
                "file=" + file +
                ", thumb=" + thumb +
                '}';
    }

    protected FileModel(Parcel in) {
        file = (FileModelDetails) in.readValue(FileModelDetails.class.getClassLoader());
        thumb = (FileModelDetails) in.readValue(FileModelDetails.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(file);
        dest.writeValue(thumb);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FileModel> CREATOR = new Parcelable.Creator<FileModel>() {
        @Override
        public FileModel createFromParcel(Parcel in) {
            return new FileModel(in);
        }

        @Override
        public FileModel[] newArray(int size) {
            return new FileModel[size];
        }
    };

}
