package de.muench.berlinstations.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Station implements Parcelable {
    private String name;
    private final StationType type;
    private final double latitude;
    private final double longitude;

    public Station(String name, StationType type, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StationType getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Double.compare(latitude, station.latitude) == 0 &&
                Double.compare(longitude, station.longitude) == 0 &&
                Objects.equals(name, station.name) &&
                type == station.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, latitude, longitude);
    }

    // -------- Parcelable implementation --------

    protected Station(Parcel in) {
        name = in.readString();
        type = StationType.valueOf(in.readString());
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type.name());
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Station> CREATOR = new Creator<>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}