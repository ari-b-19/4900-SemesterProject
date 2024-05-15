package com.metalexplorer;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.loki.afro.metallum.entity.Link;

import java.util.ArrayList;
import java.util.List;

public class ParcelableLink implements Parcelable {
    private final List<Link> userList;

    public ParcelableLink(List<Link> userList) {
        this.userList = userList;
    }

    protected ParcelableLink(Parcel in) {
        userList = new ArrayList<>();
        in.readList(userList, Link.class.getClassLoader());
    }

    public static final Creator<ParcelableLink> CREATOR = new Creator<ParcelableLink>() {
        @Override
        public ParcelableLink createFromParcel(Parcel in) {
            return new ParcelableLink(in);
        }

        @Override
        public ParcelableLink[] newArray(int size) {
            return new ParcelableLink[size];
        }
    };

    public List<Link> getLinks() {
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