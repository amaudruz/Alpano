package ch.epfl.alpano.dem;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Interval2D;
import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Distance.*;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import static ch.epfl.alpano.dem.DiscreteElevationModel.SAMPLES_PER_RADIAN;
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.dem.DiscreteElevationModel.sampleIndex;

final class ContinuousElevationModel {

    private final DiscreteElevationModel dem;
    private static final double d = toMeters(1/SAMPLES_PER_RADIAN);

    
    ContinuousElevationModel(DiscreteElevationModel dem){
        
        this.dem = requireNonNull(dem);
    }
    
    public double elevationAt(GeoPoint p){
        double xp = sampleIndex(p.longitude());
        double yp = sampleIndex(p.latitude());
        int x = (int) floor(xp);
        int y = (int) floor(yp);
        double z00 = elevationAtIndex(x,y);
        double z01 = elevationAtIndex(x, y + 1);
        double z10 = elevationAtIndex(x + 1, y);
        double z11 = elevationAtIndex(x + 1, y + 1);
        return bilerp(z00, z10, z01, z11, xp, yp);
    }
    
    public double slopeAt(GeoPoint p){
        double xp = sampleIndex(p.longitude());
        double yp = sampleIndex(p.latitude());
        int x = (int) floor(xp);
        int y = (int) floor(yp);
        double z00 = slopeAtIndex(x,y);
        double z01 = slopeAtIndex(x, y + 1);
        double z10 = slopeAtIndex(x + 1, y);
        double z11 = slopeAtIndex(x + 1, y + 1);
        return bilerp(z00, z10, z01, z11, xp, yp);
    }
    
    private double elevationAtIndex(int x, int y){
        if(dem.extent().contains(x,y)){
            return dem.elevationSample(x, y);
        }
        else{
            return 0;
        }
    }
    
    private double slopeAtIndex(int x,int y){
        return acos(d / sqrt(sq(elevationAtIndex(x,y) - elevationAtIndex(x + 1, y)) + sq(elevationAtIndex(x,y) - elevationAtIndex(x, y + 1)) + sq(d)));
    }
}
