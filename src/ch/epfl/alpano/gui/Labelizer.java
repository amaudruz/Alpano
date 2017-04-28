package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
        
        Collections.sort(visibleSummits, (x, y) -> {
            int xElevation = x.elevation();
            int yElevation = y.elevation();
            
            if(xElevation < yElevation) {
                return 1;
            }
            else if (xElevation == yElevation) {
                return 0;
            }
            else{
                return -1;
            }
        });
        
        BitSet available = new BitSet(parameters.width() - 2 * SIDE_BORDER);
        
        int height = 0;
        boolean first = true;
        
        for(Summit s : visibleSummits) {
            
            GeoPoint observerPosition = parameters.observerPosition();
            GeoPoint summitPosition = s.position();
            
            double azimuth = observerPosition.azimuthTo(summitPosition);
            double altitude = tan((s.elevation() - parameters.observerElevation())/observerPosition.distanceTo(summitPosition));

            int xForAzimuth = (int) parameters.xForAzimuth(azimuth);
            int yForAltitude = (int) parameters.yForAltitude(altitude);
            
            if(xForAzimuth > SIDE_BORDER &&  xForAzimuth < parameters.width() - SIDE_BORDER
                    && yForAltitude < parameters.height() - ABOVE_BORDER
                    && available(available, xForAzimuth)) {
                
                if(first) {
                    height = yForAltitude + LINE_LENGTH + LINE_TO_SUMMIT_PIXELS;
                    first = false;
                }
                
                labels.add(new Line(xForAzimuth, yForAltitude, xForAzimuth, height - LINE_TO_SUMMIT_PIXELS));
                
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
    
    private List<Summit> visibleSummits(PanoramaParameters parameters) {
        
        List<Summit> visibleSummits = new ArrayList<>();
        
        for(Summit s : summits) {
            
            GeoPoint observerPosition = parameters.observerPosition();
            GeoPoint summitPosition = s.position();
            
            double distanceTo = observerPosition.distanceTo(summitPosition);
            double azimuthTo = observerPosition.azimuthTo(summitPosition);
            
            ElevationProfile profile = new ElevationProfile(cem, observerPosition, azimuthTo, distanceTo);
            
            int observerElevation = parameters.observerElevation();
            int summitElevation = s.elevation();
            
            if(distanceTo <= parameters.maxDistance()
                    && abs(angularDistance(azimuthTo , parameters.centerAzimuth())) <= parameters.horizontalFieldOfView()/2d + 1e-10
                    && abs(tan((summitElevation - observerElevation)/distanceTo)) <= parameters.verticalFieldOfView()/2d
                    && Math2.firstIntervalContainingRoot(PanoramaComputer.rayToGroundDistance(profile, observerElevation, (summitElevation - observerElevation) / distanceTo), 0, distanceTo - ERROR_CONSTANT, INTERVAL) 
                        == Double.POSITIVE_INFINITY) {
                
                visibleSummits.add(s);
            }
        }
        
        return visibleSummits;
    }
}
