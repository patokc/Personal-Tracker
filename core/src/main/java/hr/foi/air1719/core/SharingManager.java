package hr.foi.air1719.core;
import android.app.Activity;
import android.app.FragmentTransaction;

/**
 * Created by DrazenVuk on 21.1.2018..
 */

public interface SharingManager {
    void share(Activity activity, FragmentTransaction fragmentTransaction, int fragment_container);
}
