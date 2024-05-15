package com.metalexplorer;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.loki.afro.metallum.entity.Band;

import java.util.ArrayList;
import java.util.List;

public class ParcelableLineup implements Parcelable {
    private final List<Band.PartialMember> lineup;

    public ParcelableLineup(List<Band.PartialMember> lineup) {
        this.lineup = lineup;
    }

    protected ParcelableLineup(Parcel in) {
        lineup = new ArrayList<>();
        in.readList(lineup, Band.PartialMember.class.getClassLoader());
    }

    public static final Creator<ParcelableLineup> CREATOR = new Creator<ParcelableLineup>() {
        @Override
        public ParcelableLineup createFromParcel(Parcel in) {
            return new ParcelableLineup(in);
        }

        @Override
        public ParcelableLineup[] newArray(int size) {
            return new ParcelableLineup[size];
        }
    };

    public List<Band.PartialMember> getlineup() {
        return lineup;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(lineup);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
