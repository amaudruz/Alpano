package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import static ch.epfl.alpano.Distance.EARTH_RADIUS;
import static ch.epfl.alpano.Math2.*;

import static java.util.Objects.requireNonNull;

import java.util.function.DoubleUnaryOperator;

/**
 * Class that computes a panorama
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 * @see Panorama
 */
public final class PanoramaComputer {
    
    private final ContinuousElevationModel dem;
    
    /**
     * Public builder
     * @param dem a continuous elevation model used to compute the panorama
     * @throws NullPointerException if the dem is null
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
       this.dem = requireNonNull(dem); 
    }

    /**
     * Function that computes the panorama
     * @param parameters the parameters of the panorama
     * @return the panorama
     */
    public Panorama computePanorama(PanoramaParameters parameters) {
        
        Panorama.Builder panoBuilder = new Panorama.Builder(parameters);
        
        //iteration on all samples
        for(int x = 0; x <= parameters.width() - 1; ++x) {
            
            ElevationProfile profile = new ElevationProfile(dem, parameters.observerPosition(), parameters.azimuthForX(x), parameters.maxDistance());
            
            double lastDistance = 0;
            
            for(int y = parameters.height() - 1; y >= 0; y--) {
               
               //not useful if it goes to infinity
               if(lastDistance != Double.POSITIVE_INFINITY) {
                   
                   //The function
                   DoubleUnaryOperator function = rayToGroundDistance(profile, parameters.observerElevation(), Math.tan(parameters.altitudeForY(y))); 
                   
                   //first approximation
                   double distance = firstIntervalContainingRoot(function, lastDistance, parameters.maxDistance(), 64);

                   //only if the first distance is finite
                   if(distance != Double.POSITIVE_INFINITY) {
                       
                       //cosinus of the angle made by the function
                       double cosAngle = Math.cos(parameters.altitudeForY(y));
                       
                       //improvement of the first approximation
                       distance = improveRoot(function, distance, distance + 64, 4);
                     
                       //distance from observer to the point
                       double distanceTo = distance/cosAngle;
                       
                       //set all found datum
                       panoBuilder.setDistanceAt(x,y,(float) distanceTo )
                       .setElevationAt(x, y, (float) profile.elevationAt(distance))
                       .setLatitudeAt(x, y,(float) profile.positionAt(distance).latitude())
                       .setLongitudeAt(x, y,(float) profile.positionAt(distance).longitude())
                       .setSlopeAt(x, y, (float) profile.slopeAt(distance));
                       
                   }
                   
                   lastDistance = distance;
               }
            }
        }
        
        return panoBuilder.build();
    }
    
    /**
     * Give a function computing the distance between a ray and the ground
     * @param profile 
     * @param ray0 initial elevation
     * @param raySlope slope of the function
     * @return a function computing the distance between the ground and the ray
     * @throws NullPointerException if profile is null
     */
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {
        requireNonNull(profile);
        return x -> 
            ray0 +  x * raySlope - profile.elevationAt(x) + sq(x) * ((1.0 - 0.13)/(2 * EARTH_RADIUS ));
        
    }
}
