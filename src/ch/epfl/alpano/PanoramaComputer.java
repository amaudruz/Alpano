package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import static ch.epfl.alpano.Distance.EARTH_RADIUS;
import static ch.epfl.alpano.Math2.*;

import static java.util.Objects.requireNonNull;

import java.util.function.DoubleUnaryOperator;

public final class PanoramaComputer {
    
    private final ContinuousElevationModel dem;
    
    public PanoramaComputer(ContinuousElevationModel dem) {
       this.dem = requireNonNull(dem); 
    }

    public Panorama computePanorama(PanoramaParameters parameters) {
        Panorama.Builder panoBuilder = new Panorama.Builder(parameters);
        
        for(int x = 0; x <= parameters.width() - 1; ++x) {
            ElevationProfile profile = new ElevationProfile(dem, parameters.observerPosition(), parameters.azimuthForX(x), parameters.maxDistance());

            for(int y = parameters.height() - 1; y >= 0; y--) {
               DoubleUnaryOperator function = rayToGroundDistance(profile, parameters.observerElevation(), Math.tan(parameters.altitudeForY(y))); 
               double distance = firstIntervalContainingRoot(function, 0, parameters.maxDistance() - 70, 64);
               if(distance == Double.POSITIVE_INFINITY){
                   break;
               }
               distance = improveRoot(function, distance, distance + 64, 4);
               panoBuilder.setDistanceAt(x,y,(float) distance)
               .setElevationAt(x, y, (float) profile.elevationAt(distance))
               .setLatitudeAt(x, y,(float) profile.positionAt(distance).latitude())
               .setLongitudeAt(x, y,(float) profile.positionAt(distance).longitude())
               .setSlopeAt(x, y, (float) profile.slopeAt(distance));
               
            }
        }
        
        return panoBuilder.build();
    }
    
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {
        DoubleUnaryOperator function = (double x) -> {
            return ray0 +  x * raySlope - profile.elevationAt(x) + sq(x) * (1 - 0.13)/(2 * EARTH_RADIUS );
        };
        
        return function;
    }
}
