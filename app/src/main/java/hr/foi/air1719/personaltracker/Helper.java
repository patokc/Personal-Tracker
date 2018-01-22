package hr.foi.air1719.personaltracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Helper {

    public static void PushNotificationGPS(Context context) {
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "M_CH_ID");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ikona)
                .setTicker("PersonalTracker")
                .setSound(notificationSound)
                .setVibrate(new long[] { 1000, 1000})

                .setContentTitle("GPS is off")
                .setContentText("Please turn on GPS!")
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }


    public static void PushNotificationInternet(Context context) {
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "M_CH_ID");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ikona)
                .setTicker("PersonalTracker")
                .setSound(notificationSound)
                .setVibrate(new long[] { 1000, 1000})

                .setContentTitle("No internet connection")
                .setContentText("Turn on your network connection.")
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }



    public static final Pattern letters_only_check =
            Pattern.compile(    "^[a-zA-Z\\s]*$", Pattern.CASE_INSENSITIVE);



    public static boolean validateLetters(String letters) {
        Matcher matcher = letters_only_check.matcher(letters);
        return matcher.find();
    }

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
