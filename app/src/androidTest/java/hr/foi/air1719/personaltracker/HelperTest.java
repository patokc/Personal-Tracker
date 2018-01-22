package hr.foi.air1719.personaltracker;
import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;

import hr.foi.air1719.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by DrazenVuk on 17.1.2018..
 */
public class HelperTest {


    private Context instrumentationCtx;

    @Before
    public void setup() {
        instrumentationCtx = InstrumentationRegistry.getContext();
    }


    @Test
    public void md5() throws Exception {

        String actual = Helper.md5("Ovo je test");
        String expected = "53c19d6697e9f395764013d783189586";
        assertEquals("MD5 failed", expected, actual);
    }

    @Test
    public void isValidEmail() throws Exception {
        boolean actual = Helper.isValidEmail("drazen@gmail.com");
        boolean expected = true;
        assertEquals("IsValidEmail failed", expected, actual);

        actual = Helper.isValidEmail("gm645ail.com");
        expected = false;
        assertEquals("IsValidEmail failed", expected, actual);
    }

    @Test
    public void isInternetAvailable() throws Exception {
        boolean actual = Helper.isInternetAvailable(instrumentationCtx);
        boolean expected = true;
        assertEquals("Internet failed", expected, actual);
    }

    @Test
    public void calculateDistance() throws Exception {

        Location l1= new Location("dummyprovider");
        Location l2= new Location("dummyprovider");

        l1.setLatitude(100.5);
        l1.setLongitude(200.66);

        l2.setLatitude(200.5);
        l2.setLongitude(300.66);

        float actual = Helper.CalculateDistance(l1, l2);
        float expected = 12433;
        assertEquals("CalculateDistance failed", expected, actual,1.01f);

    }

    @Test
    public void calculateAvgSpeed() throws Exception {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR,-1);

        float actual = Helper.CalculateAvgSpeed(now.getTime(), new Date(), 100);
        float expected = 100;
        assertEquals("CalculateAvgSpeed failed", expected, actual,1.01f);
    }

}