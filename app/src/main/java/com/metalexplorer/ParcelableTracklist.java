package com.metalexplorer;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Track;

import java.util.ArrayList;
import java.util.List;

public class ParcelableTracklist implements Parcelable {
    private final List<Track> trackList;

    public ParcelableTracklist(List<Track> userList) {
        this.trackList = userList;
    }

    protected ParcelableTracklist(Parcel in) {
        trackList = new ArrayList<>();
        in.readList(trackList, Disc.class.getClassLoader());
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

    public List<Track> getUserList() {
        return trackList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(trackList);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}