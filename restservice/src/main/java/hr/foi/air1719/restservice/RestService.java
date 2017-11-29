package hr.foi.air1719.restservice;

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

    @POST("location/{user}.json")
    Call<Location> saveLocation(@Body Location data, @Path("user") String user);
}
