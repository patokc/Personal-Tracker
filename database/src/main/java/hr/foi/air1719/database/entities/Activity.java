package hr.foi.air1719.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by abenkovic on 11/25/17.
 */

@Entity
public class Activity {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String activityId;

    @ColumnInfo(name = "user")
    private String user;

    @ColumnInfo(name = "mode")
    private ActivityMode mode;

    @ColumnInfo(name = "start")
    private Timestamp start;

    @ColumnInfo(name = "finish")
    private Timestamp finish;

    @ColumnInfo(name = "averageSpeed")
    private float averageSpeed;

    @ColumnInfo(name = "distance")
    private float distance;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "avgFuel")
    private float avgFuel;

    @ColumnInfo(name = "avgCal")
    private float avgCal;

    public Activity(ActivityMode mode) {
        this.activityId = UUID.randomUUID().toString();
        this.mode = mode;
        this.start = new Timestamp(System.currentTimeMillis());
    }

    @Ignore
    public Activity(@NonNull String activityId, String user, ActivityMode mode, Timestamp start, Timestamp finish, float averageSpeed, float distance, String description, String image) {
        this.activityId = activityId;
        this.user = user;
        this.mode = mode;
        this.start = start;
        this.finish = finish;
        this.averageSpeed = averageSpeed;
        this.distance = distance;
        this.description = description;
        this.image = image;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ActivityMode getMode() {
        return mode;
    }

    public void setMode(ActivityMode mode) {
        this.mode = mode;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getFinish() {
        return finish;
    }

    public void setFinish(Timestamp finish) {
        this.finish = finish;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getAvgFuel() {
        return avgFuel;
    }

    public void setAvgFuel(float avgFuel) {
        this.avgFuel = avgFuel;
    }

    public float getAvgCal() {
        return avgCal;
    }

    public void setAvgCal(float avgCal) {
        this.avgCal = avgCal;
    }
}
