package com.metalexplorer;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.loki.afro.metallum.entity.Disc;

import java.util.ArrayList;
import java.util.List;

public class ParcelableDisc implements Parcelable {
    private final List<Disc> userList;

    public ParcelableDisc(List<Disc> userList) {
        this.userList = userList;
    }

    protected ParcelableDisc(Parcel in) {
        userList = new ArrayList<>();
        in.readList(userList, Disc.class.getClassLoader());
    }

    public static final Creator<ParcelableDisc> CREATOR = new Creator<ParcelableDisc>() {
        @Override
        public ParcelableDisc createFromParcel(Parcel in) {
            return new ParcelableDisc(in);
        }

        @Override
        public ParcelableDisc[] newArray(int size) {
            return new ParcelableDisc[size];
        }
    };

    public List<Disc> getUserList() {
        return userList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(userList);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}