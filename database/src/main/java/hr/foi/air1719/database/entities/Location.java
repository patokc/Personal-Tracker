package hr.foi.air1719.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by abenkovic on 10/28/17.
 */

@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int locationId;

    @ColumnInfo(name = "userId")
    private int userId;


    @ColumnInfo(name = "longitude")
    private long longitude;


    @ColumnInfo(name = "latitude")
    private long latitude;


    @ColumnInfo(name = "gpsType")
    private int gpsType;

    @ColumnInfo(name = "timestamp")
    private Date timestamp;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
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
}
