package hr.foi.air1719.location;

import android.app.Fragment;
import android.app.Instrumentation;
import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;



@RunWith(AndroidJUnit4.class)
public class MyLocationTest extends android.app.Fragment implements IGPSActivity{

    @Test
    public void GetLastKnownLocation() throws Exception {

        Context appContext = InstrumentationRegistry.getTargetContext();
        Fragment f = new Fragment();

        MyLocation myLocation = new MyLocation();
        Location location = myLocation.GetLastKnownLocation(this);

        assertEquals("Error with location.", location, null);
    }

    @Override
    public void locationChanged(Location location) {

    }
}