package hr.foi.air1719.location;

/**
 * Created by DrazenVuk on 16.11.2017..
 */

public interface IGPSActivity {
    void locationChanged(double longitude, double latitude);
    void displayGPSSettingsDialog();
}
