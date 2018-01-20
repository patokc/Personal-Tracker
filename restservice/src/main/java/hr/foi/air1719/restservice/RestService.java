package hr.foi.air1719.restservice;

import com.squareup.okhttp.RequestBody;

import java.util.Map;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.restservice.responses.ImageResponse;
import hr.foi.air1719.restservice.responses.UserResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by abenkovic on 10/29/17.
 */

public interface RestService {
    @GET("users/{user}.json")
    Call<UserResponse> getUser(@Path("user") String user);

    @DELETE("users/{user}.json")
    Call<UserResponse> deleteUser(@Path("user") String user);

    @PUT("users/{user}.json")
    Call<User> createUser(@Body User data, @Path("user") String user);

    @PUT("activities/{user}/{activityId}.json")
    Call<Activity> saveActivity(@Body Activity data, @Path("user") String user, @Path("activityId") String activityId);

    @GET("activities/{user}/{activityId}.json")
    Call<Activity> getActivity(@Path("user") String user, @Path("activityId") String activityId);

    @GET("activities/{user}.json")
    Call<Map<String, Activity>> getAllActivities(@Path("user") String user);

    @PUT("gpsLocations/{user}/{gpsLocationId}.json")
    Call<GpsLocation> saveLocation(@Body GpsLocation data, @Path("user") String user, @Path("gpsLocationId") String gpsLocationId);

    @GET("gpsLocations/{user}.json?orderBy=\"activityId\"&equalTo=\"{activityId}.json\"")
    Call<Map<String, GpsLocation>> getLocations(@Path("user") String user, @Path("activityId") String activityId);

    @GET("gpsLocations/{user}.json")
    Call<Map<String, GpsLocation>> getAllLocations(@Path("user") String user);

    @Multipart
    @POST("https://benkovic.net/air/upload.php")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part image);

}