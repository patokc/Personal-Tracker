package hr.foi.air1719.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by abenkovic on 10/28/17.
 */

@Entity
public class GpsLocation {
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

    @ColumnInfo(name = "accuracy")
    private float accuracy;

    @ColumnInfo(name = "timestamp")
    private Date timestamp;

    public GpsLocation(String username, int activityId, double longitude, double latitude, float accuracy, int gpsType) {
        this.username = username;
        this.activityId = activityId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = new Date();
        this.accuracy = accuracy;
        this.gpsType=gpsType;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public float getAccuracy() { return accuracy; }

    public void setAccuracy(float accuracy) { this.accuracy = accuracy; }
}
