package hr.foi.air1719.restservice.responses;

/**
 * Created by abenkovic on 12/22/17.
 */

public class ImageResponse {

    private String name;
    private String path;
    private boolean error;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
