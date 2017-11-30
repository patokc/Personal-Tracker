package hr.foi.air1719.database.converters;

import android.arch.persistence.room.TypeConverter;

import java.sql.Timestamp;
import java.util.Date;


public class DateConverter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String timestampToString(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toString();
    }

    @TypeConverter
    public static Timestamp stringToTimestamp(String timestamp) {
        return timestamp == null ? null : Timestamp.valueOf(timestamp);
    }

}
