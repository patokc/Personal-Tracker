package hr.foi.air1719.restservice;

/**
 * Created by abenkovic
 */

public interface RestServiceHandler {
    void onDataArrived(Object result, boolean ok);
}
