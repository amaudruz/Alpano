package ch.epfl.alpano;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.*;

import java.util.Locale;

import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Distance.*;
import static ch.epfl.alpano.Azimuth.*;
/**
 * Represents a point on earth surface
 * (immutable class)
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 *
 */
public final class GeoPoint {

    private final double longitude;
    private final double latitude;
    
    /**
     * Builder
     * @param longitude
     * @param latitude
     * @throws IllegalArgumentException if longitude or latitude is not properly formated
     */
    public GeoPoint(double longitude, double latitude){
        checkArgument(longitude <= PI && longitude >= -PI);
        checkArgument(latitude <= PI/2 && latitude >= -PI/2);
        
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    /**
     * 
     * @return its longitude
     */
    public double longitude(){
        return longitude;
    }
    
    /**
     * 
     * @return its latitude
     */
    public double latitude(){
        return latitude;
    }
    
    /**
     * Compute the distance between two points
     * @param that - an other point
     * @throws NullPointerException if that is null
     * @return the distance in meters
     */
    public double distanceTo(GeoPoint that){
        /*double angle = 2 * asin(sqrt(
                haversin(angularDistance(latitude, that.latitude) + cos(latitude)*cos(that.latitude)*haversin(angularDistance(longitude, that.longitude)))
                ));*/
        if(that == null){
            throw new NullPointerException();
        }
        double angle = 2 * asin(sqrt(haversin(latitude- that.latitude) + cos(latitude)*cos(that.latitude)*haversin(longitude- that.longitude)));
        return toMeters(angle);
    }
    
    /**
     * Give the azimuth from the current to a given position
     * @param that - the other position
     * @throws NullPointerException if that is null
     * @return the azimuth
     */
    public double azimuthTo(GeoPoint that){
        if(that == null){
            throw new NullPointerException();
        }
        double angle = atan2(sin(longitude - that.longitude)*cos(that.latitude),
                (cos(latitude)*sin(that.latitude)-sin(latitude)*cos(that.latitude)*cos(longitude-that.longitude)));
        //System.out.println(angle);
        return fromMath(canonicalize(angle));
    }
    

    @Override
    public String toString(){
       
        //double latDegree = floor(latitude * 180/PI * 1000) / 1000;
        //double lonDegree = floor(longitude * 180/PI * 1000) / 1000;
        double latDegree =latitude * 180/PI;
        double lonDegree = longitude * 180/PI;
        Locale l = null;
        return String.format(l,"[%.3f; %.3f]", lonDegree, latDegree);
    }
    
}
