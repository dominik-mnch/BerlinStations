package de.muench.berlinstations.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Station implements Parcelable {

    private final String name;
    private final double latitude;
    private final double longitude;
    private final int difficulty;

    public Station(String name, double latitude, double longitude, int difficulty) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Double.compare(latitude, station.latitude) == 0 && Double.compare(longitude, station.longitude) == 0 && difficulty == station.difficulty && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, latitude, longitude, difficulty);
    }

    // -------- Parcelable implementation --------

    protected Station(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        difficulty = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(difficulty);
    }

    @Override
    public int describeContents() {
        return 0; // usually 0
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
