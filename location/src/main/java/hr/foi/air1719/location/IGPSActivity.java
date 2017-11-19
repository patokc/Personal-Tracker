package hr.foi.air1719.location;

import android.location.Location;

/**
 * Created by DrazenVuk on 16.11.2017..
 */

public interface IGPSActivity {
    void locationChanged(Location location);
    void displayGPSSettingsDialog();
}
