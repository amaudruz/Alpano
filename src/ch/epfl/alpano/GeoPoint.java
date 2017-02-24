package ch.epfl.alpano;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.*;

import java.util.Locale;

import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Distance.*;
import static ch.epfl.alpano.Azimuth.*;
/**
 * Represents a point on earth surface
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 *
 */
public final class GeoPoint {
    final Sitring s;
    final double longitude;
    final double latitude;
    
    /**
     * Builder
     * @param longitude
     * @param latitude
     * @throws IllegalArgumentException if longitude or latitude is not properly formated
     */
    GeoPoint(double longitude, double latitude){
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
     * @return the distance in meters
     */
    public double distanceTo(GeoPoint that){
        /*double angle = 2 * asin(sqrt(
                haversin(angularDistance(latitude, that.latitude) + cos(latitude)*cos(that.latitude)*haversin(angularDistance(longitude, that.longitude)))
                ));*/
        double angle = 2 * asin(sqrt(haversin(latitude- that.latitude) + cos(latitude)*cos(that.latitude)*haversin(longitude- that.longitude)));
        return toMeters(angle);
    }
    
    /**
     * Give the azimuth from the current to a given position
     * @param that - the other position
     * @return the azimuth
     */
    public double azimuthTo(GeoPoint that){
        double angle = atan2(sin(longitude - that.longitude)*cos(that.latitude),(cos(latitude)*sin(that.latitude)-sin(latitude)*cos(that.latitude)*cos(longitude-that.longitude)));
        System.out.println(angle);
        return fromMath(canonicalize(angle));
    }
    

    @Override
    public String toString(){
       
        double latDegree = floor(latitude * 180/PI * 1000) / 1000;
        double lonDegree = floor(longitude * 180/PI * 1000) / 1000;
        Locale l = null;
        return String.format(l,"[%.4f; %.4f]", lonDegree, latDegree);
    }
    
}
