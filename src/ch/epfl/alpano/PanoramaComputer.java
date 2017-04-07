package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import static ch.epfl.alpano.Distance.EARTH_RADIUS;
import static ch.epfl.alpano.Math2.*;

import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;

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
     * Create a panorama computer from a continuous dem
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
            
            double lastAbcissa = 0;
            
            for(int y = parameters.height() - 1; y >= 0; y--) {
               
               //not useful if it goes to infinity
               if(lastAbcissa != Double.POSITIVE_INFINITY) {
                   
                   //The function
                   DoubleUnaryOperator function = rayToGroundDistance(profile, parameters.observerElevation(), tan(parameters.altitudeForY(y))); 
                   
                   //first approximation
                   double abcissa = firstIntervalContainingRoot(function, lastAbcissa, parameters.maxDistance(), 64);

                   //only if the first distance is finite
                   if(abcissa != Double.POSITIVE_INFINITY) {
                       
                       //improvement of the first approximation
                       abcissa = improveRoot(function, abcissa, abcissa + 64, 4);
                     
                       //distance from observer to the point, using the angle between the function and the axe
                       double distance = abcissa/cos(parameters.altitudeForY(y));
                       
                       //set all found datum
                       panoBuilder.setDistanceAt(x,y,(float) distance )
                       .setElevationAt(x, y, (float) profile.elevationAt(abcissa))
                       .setLatitudeAt(x, y,(float) profile.positionAt(abcissa).latitude())
                       .setLongitudeAt(x, y,(float) profile.positionAt(abcissa).longitude())
                       .setSlopeAt(x, y, (float) profile.slopeAt(abcissa));
                       
                   }
                   
                   lastAbcissa = abcissa;
               }
            }
        }
        
        return panoBuilder.build();
    }
    
    /**
     * Give a function computing the distance between a ray and the ground
     * @param profile the profile
     * @param ray0 initial elevation
     * @param raySlope slope of the function
     * @return a function computing the distance between the ground and the ray
     * @throws NullPointerException if profile is null
     */
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {
        requireNonNull(profile);
        return x -> 
            ray0 + x * raySlope - profile.elevationAt(x) + sq(x) * ((1.0 - 0.13) / (2 * EARTH_RADIUS ));
        
    }
}
