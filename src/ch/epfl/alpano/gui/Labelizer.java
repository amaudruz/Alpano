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
import javafx.scene.transform.Translate;

import static ch.epfl.alpano.Math2.angularDistance;
import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Represents a Tagging of the panorama (all the summits to be drawn in the panorama)
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */
public final class Labelizer {

    
    private final ContinuousElevationModel cem;
    private final List<Summit> summits;
    

   /**
    * Construct the labelizer given a continuous elevation model and a list of all the summits
    * @param cem the continuous elevation model
    * @param summits the list of all the summits
    */
    public Labelizer(ContinuousElevationModel cem, List<Summit> summits) {
        this.cem = requireNonNull(cem);
        this.summits = Collections.unmodifiableList(requireNonNull(summits));
    }
    
    private static final int ABOVE_BORDER = 170;
    private static final int LINE_TO_SUMMIT_PIXELS = 2;
    private static final int SIDE_BORDER = 20;
    private static final int LINE_LENGTH = 20;
    private static final int TEXT_ROTATION = -60;

    
    /**
     * Construct a list of nodes representing all the summits that can be drawn given some constraints
     * in a panorama
     * @param parameters the parameters of the panorama
     * @return the list of nodes
     */
    public List<Node> labels(PanoramaParameters parameters) {
        
        List<Node> labels = new ArrayList<>();
        List<VisibleSummit> visibleSummits = visibleSummits(parameters);

        
        Collections.sort(visibleSummits, (x, y) -> {
            
            int Yx = x.getY();
            int Yy = y.getY();
            
            if(Yx > Yy) {
                return 1;
            }
            else if (Yx < Yy) {
                return -1;
            }
            else{
                
                int xElevation = x.getSummit().elevation();
                int yElevation = y.getSummit().elevation();
                
                if(xElevation < yElevation) {
                    return 1;
                }
                else if(xElevation > yElevation) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
        
        int width = parameters.width();
        
        BitSet available = new BitSet(width - 2 * SIDE_BORDER); 
        
        int minHeight = 0;
        boolean first = true;
        
        for(VisibleSummit s : visibleSummits) {
            
            int x = s.getX();
            int y = s.getY();
            
            if(x >= SIDE_BORDER 
                    && x <= (width - SIDE_BORDER)
                    && y >= ABOVE_BORDER
                    && available(available, x - SIDE_BORDER)) {
                
                if(first) {
                    minHeight = y - LINE_LENGTH;
                    first = false;
                }
                
                labels.add(new Line(x, minHeight, x, y));
                
                //TODO
                Summit summit = s.getSummit();
                Text text = new Text(summit.name() + " (" + summit.elevation() + ")");
                text.getTransforms().addAll(new Translate(x, minHeight), new Rotate(TEXT_ROTATION));
                labels.add(text);
            }
        }

        
        return labels;
    }
    
    
    //  Indicates if the certain area of a set of bits is available
     
    private static final int SPACE = 20;
    
    private boolean available(BitSet set, int index) {
        
        
        for(int i = index; i < index + SPACE; i++) {
            
            if(set.get(i)) {
                return false;
            }
        }
       
        set.set(index, index + SPACE, true);

        return true;
    }
    
    
    private static final int ERROR_CONSTANT = 200;
    private static final int INTERVAL = 64;
   
    //Construct a list of all the visible summits in a panorama
     
    private List<VisibleSummit> visibleSummits(PanoramaParameters parameters) {

        
        List<VisibleSummit> visibleSummits = new ArrayList<>();
        
        for(Summit s : summits) {
            
            GeoPoint observerPosition = parameters.observerPosition();
            GeoPoint summitPosition = s.position();
            
            double distanceTo = observerPosition.distanceTo(summitPosition);
            double azimuthTo = observerPosition.azimuthTo(summitPosition);
            
            ElevationProfile profile = new ElevationProfile(cem, observerPosition, azimuthTo, distanceTo);
            
            int observerElevation = parameters.observerElevation();
            
            double h = PanoramaComputer.rayToGroundDistance(profile, observerElevation, 0).applyAsDouble(distanceTo);
            double slope = -h / distanceTo;
            
            DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(profile, observerElevation, slope);
            
            double altitude = atan(slope);
            
            if(distanceTo <= parameters.maxDistance()
                    && abs(angularDistance(parameters.centerAzimuth(), azimuthTo)) <= parameters.horizontalFieldOfView()/2d
                    && abs(altitude) <= parameters.verticalFieldOfView()/2d
                    && Math2.firstIntervalContainingRoot(f, 0, distanceTo, INTERVAL) >= distanceTo - ERROR_CONSTANT
                    ) {
                int x = (int) round(parameters.xForAzimuth(azimuthTo));
                int y = (int) round(parameters.yForAltitude(altitude));
                visibleSummits.add(new VisibleSummit(s, x, y));
            }
        }
        
        return visibleSummits;
    }
    
    //Ease the access to data that have already been calculated
    private static final class VisibleSummit {
        private final Summit summit;
        private final int x;
        private final int y;
        
        private VisibleSummit(Summit s, int x, int y) {
            summit = s;
            this.x = x;
            this.y = y;
        }
        
        private int getX() {
            return x;
        }
        
        private int getY() {
            return y;
        }
        
        private Summit getSummit() {
            return summit;
        }
    }
}
