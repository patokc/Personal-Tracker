package hr.foi.air1719.database.converters;

import android.arch.persistence.room.TypeConverter;

import hr.foi.air1719.database.entities.ActivityMode;

/**
 * Created by abenkovic on 11/26/17.
 */

public class ActivityModeConverter {
    @TypeConverter
    public static String Mode_eToString(ActivityMode mode) {
        return mode == null ? null : mode.toString();
    }

    @TypeConverter
    public static ActivityMode StringToMode_e(String mode) {
        return mode == null ? null : ActivityMode.valueOf(mode);
    }
}
