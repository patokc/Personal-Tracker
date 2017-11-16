package hr.foi.air1719.location;

/**
 *  Created by DrazenVuk on 16.11.2017...
 */

public class CurrentLocation {
    double londitude;
    double lantitude;
    Integer accuracy;
    TypeOfGpsData_e Type;


    public double getLonditude() {
        return londitude;
    }

    public void setLonditude(double londitude) {
        this.londitude = londitude;
    }

    public double getLantitude() {
        return lantitude;
    }

    public void setLantitude(double lantitude) {
        this.lantitude = lantitude;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public TypeOfGpsData_e getType() {
        return Type;
    }

    public void setType(TypeOfGpsData_e type) {
        Type = type;
    }
}
