package hr.foi.air1719.personaltracker;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


public class Helper {

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static boolean isInternetAvailable(Context context) {
        try {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return false;
    }

    public static float CalculateDistance(Location startPoint, Location endPoint)
    {
        return (startPoint.distanceTo(endPoint) / ((float)1000));
    }

    public static float CalculateAvgSpeed(Date startDate, Date endDate, double KM)
    {
        try {
            long different = endDate.getTime() - startDate.getTime();

            float diffHours = (float)((float)different / (float)(60 * 60 * 1000));
            if(diffHours==0) return 0;

            return (float)(KM / diffHours);
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
        return -1;
    }

}
