package hr.foi.air1719.restservice.responses;

import hr.foi.air1719.database.entities.User;

/**
 * Created by abenkovic
 */

public class UserResponse extends User {
    public UserResponse(String username, String password, String fullname) {
        super(username, password, fullname);
    }
}
