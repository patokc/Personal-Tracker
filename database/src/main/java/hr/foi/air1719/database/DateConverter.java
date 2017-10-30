package hr.foi.air1719.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by abenkovic on 10/28/17.
 */

public class DateConverter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
