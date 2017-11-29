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

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "activityId")
    private int activityId;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "gpsType")
    private int gpsType;

    @ColumnInfo(name = "timestamp")
    private Timestamp timestamp;

    public Location(String username, int activityId, double longitude, double latitude) {
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
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
