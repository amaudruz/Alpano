package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import static ch.epfl.alpano.Math2.angularDistance;
import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.BitSet;

public final class Labelizer {

    
    private final ContinuousElevationModel cem;
    private final List<Summit> summits;
    
    
    public Labelizer(ContinuousElevationModel cem, List<Summit> summits) {
        this.cem = requireNonNull(cem);
        this.summits = Collections.unmodifiableList(requireNonNull(summits));
    }
    
    private static final int ABOVE_BORDER = 170;
    private static final int LINE_TO_SUMMIT_PIXELS = 2;
    private static final int SIDE_BORDER = 20;
    private static final int LINE_LENGTH = 20;
    private static final int TEXT_ROTATION = 60;
    
    public List<Node> labels(PanoramaParameters parameters) {
        
        List<Node> labels = new ArrayList<>();
        List<Summit> visibleSummits = visibleSummits(parameters);
        
        GeoPoint observerPosition = parameters.observerPosition();
        
        Collections.sort(visibleSummits, (x, y) -> {
            
            double altitudeX = tan((x.elevation() - parameters.observerElevation())/observerPosition.distanceTo(x.position()));
            double altitudeY = tan((y.elevation() - parameters.observerElevation())/observerPosition.distanceTo(y.position()));

            int yForAltitudeX = (int) round(parameters.yForAltitude(altitudeX));
            int yForAltitudeY = (int) round(parameters.yForAltitude(altitudeY));
            
            if(yForAltitudeX < yForAltitudeY) {
                return 1;
            }
            else if (yForAltitudeX > yForAltitudeY) {
                return -1;
            }
            else{
                if(x.elevation() < y.elevation()) {
                    return 1;
                }
                else if(x.elevation() > y.elevation()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
        
        BitSet available = new BitSet(parameters.width() - 2 * SIDE_BORDER);
        
        int height = 0;
        boolean first = true;
        
        for(Summit s : visibleSummits) {
            
            
            GeoPoint summitPosition = s.position();
            
            double azimuth = observerPosition.azimuthTo(summitPosition);
            double altitude = tan((s.elevation() - parameters.observerElevation())/observerPosition.distanceTo(summitPosition));

            int xForAzimuth = (int) parameters.xForAzimuth(azimuth);
            int yForAltitude = (int) parameters.yForAltitude(altitude);
            
            if(xForAzimuth > SIDE_BORDER &&  xForAzimuth < parameters.width() - SIDE_BORDER
                    && yForAltitude > ABOVE_BORDER
                    && available(available, xForAzimuth)) {
                
                if(first) {
                    height = yForAltitude + LINE_LENGTH + LINE_TO_SUMMIT_PIXELS;
                    first = false;
                }
                
                labels.add(new Line(xForAzimuth, height, xForAzimuth, yForAltitude - LINE_TO_SUMMIT_PIXELS));
                
                Text text = new Text(xForAzimuth, height, s.name());
                text.getTransforms().add(new Rotate(TEXT_ROTATION, 0, 0));
                labels.add(text);
            }
        }

        
        return labels;
    }
    
    private boolean available(BitSet set, int index) {
        
        boolean available = true;
        
        for(int i = index; i < index + SIDE_BORDER; i++) {
            
            if(!set.get(i)) {
                available = false;
            }
        }
        
        return available;
    }
    
    
    private static final int ERROR_CONSTANT = 200;
    private static final int INTERVAL = 64;
    private static final double EPSILON = 4;
    
    //doit etre private
    public List<Summit> visibleSummits(PanoramaParameters parameters) {
        
        List<Summit> visibleSummits = new ArrayList<>();
        
        for(Summit s : summits) {
            
            GeoPoint observerPosition = parameters.observerPosition();
            GeoPoint summitPosition = s.position();
            
            double distanceTo = observerPosition.distanceTo(summitPosition);
            double azimuthTo = observerPosition.azimuthTo(summitPosition);
            
            ElevationProfile profile = new ElevationProfile(cem, observerPosition, azimuthTo, distanceTo);
            
            int observerElevation = parameters.observerElevation();
            int summitElevation = s.elevation();
            //System.out.println(observerElevation + " " + summitElevation + " " + (summitElevation - observerElevation) / distanceTo);
            DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(profile, observerElevation, (summitElevation - observerElevation) / distanceTo);
            
            //System.out.println(Math2.firstIntervalContainingRoot(f, 0, distanceTo, INTERVAL) +" " + (distanceTo - ERROR_CONSTANT));
            if(distanceTo <= parameters.maxDistance()
                    && abs(angularDistance(azimuthTo , parameters.centerAzimuth())) <= parameters.horizontalFieldOfView()/2d + 1e-10
                    && abs(tan((summitElevation - observerElevation)/distanceTo)) <= parameters.verticalFieldOfView()/2d
                    && Math2.improveRoot(f, Math2.firstIntervalContainingRoot(f, 0, distanceTo, INTERVAL), distanceTo, EPSILON) >= distanceTo - ERROR_CONSTANT
                    ) {

                visibleSummits.add(s);
            }
        }
        
        return visibleSummits;
    }
}
