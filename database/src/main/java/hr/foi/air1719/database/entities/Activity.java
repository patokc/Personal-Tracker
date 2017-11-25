package hr.foi.air1719.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.util.Date;

/**
 * Created by abenkovic on 11/25/17.
 */

@Entity
public class Activity {

    @PrimaryKey(autoGenerate = true)
    private int activityId;

    @ColumnInfo(name = "mode")
    private ActivityMode mode;

    @ColumnInfo(name = "start")
    private Date start;

    @ColumnInfo(name = "finish")
    private Date finish;

    @ColumnInfo(name = "averageSpeed")
    private float averageSpeed;

    @ColumnInfo(name = "distance")
    private float distance;

    public Activity(ActivityMode mode) {
        this.mode = mode;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public ActivityMode getMode() {
        return mode;
    }

    public void setMode(ActivityMode mode) {
        this.mode = mode;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
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
}
