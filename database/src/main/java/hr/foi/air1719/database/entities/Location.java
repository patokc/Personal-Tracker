package hr.foi.air1719.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by abenkovic on 10/28/17.
 */

@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int locationId;

    @ColumnInfo(name = "activityId")
    private String activityId;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "gpsType")
    private int gpsType;

    @ColumnInfo(name = "timestamp")
    private Timestamp timestamp;

    public Location(String activityId, double longitude, double latitude) {
        this.activityId = activityId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getGpsType() {
        return gpsType;
    }

    public void setGpsType(int gpsType) {
        this.gpsType = gpsType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
