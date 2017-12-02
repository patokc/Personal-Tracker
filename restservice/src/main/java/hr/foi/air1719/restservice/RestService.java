package hr.foi.air1719.restservice;

import java.util.Map;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.restservice.responses.UserResponse;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by abenkovic on 10/29/17.
 */

public interface RestService {
    @GET("users/{user}.json")
    Call<UserResponse> getUser(@Path("user") String user);

    @PUT("users/{user}.json")
    Call<User> createUser(@Body User data, @Path("user") String user);

    @PUT("activities/{user}/{mode}/{activityId}.json")
    Call<Activity> saveActivity(@Body Activity data, @Path("user") String user, @Path("activityId") String activityId, @Path("mode") ActivityMode mode);

    @GET("activities/{user}/{mode}/{activityId}.json")
    Call<Activity> getActivity(@Path("user") String user, @Path("mode") ActivityMode mode, @Path("activityId") String activityId);

    @GET("activities/{user}/{mode}.json")
    Call<Map<String, Activity>> getAllActivities(@Path("user") String user, @Path("mode") ActivityMode mode);

    @PUT("gpsLocations/{user}/{activityId}/{gpsLocationId}.json")
    Call<GpsLocation> saveLocation(@Body GpsLocation data, @Path("user") String user, @Path("activityId") String activityId, @Path("gpsLocationId") String gpsLocationId);

    @GET("gpsLocations/{user}/{activityId}.json")
    Call<Map<String, GpsLocation>> getLocations(@Path("user") String user, @Path("activityId") String activityId);




}