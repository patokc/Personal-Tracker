package hr.foi.air1719.restservice;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.Location;
import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.restservice.responses.UserResponse;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
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

    @PUT("{user}/{activityId}.json")
    Call<Location> saveLocation(@Body Location data, @Path("user") String user, @Path("activityId") String activityId);

    @GET("{user}/{activityId}.json")
    Call<Location> getLocation(@Path("user") String user, @Path("activityId") String activityId);

    @GET("activities/{user}/{mode}/{activityId}.json")
    Call<Activity> getActivity(@Path("user") String user, @Path("mode") ActivityMode mode, @Path("activityId") String activityId);

    @GET("activities/{user}/{mode}.json")
    Call<Activity> getAllActivities(@Path("user") String user, @Path("mode") ActivityMode mode);

    @PUT("activities/{user}/{mode}/{activityId}.json")
    Call<Activity> saveActivity(@Body Activity data, @Path("user") String user, @Path("activityId") String activityId, @Path("mode") ActivityMode mode);
}
