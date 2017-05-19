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
    
<<<<<<< HEAD
   /**
    * Construc the labelizer given a continous elevation model and a list of all the summits
    * @param cem the continious elevaition model
    * @param summits the list of all the summits
    */
=======
    
>>>>>>> 4f9e7fefe1245b7dec98fac0f2c11892429d9841
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
     * Construct a list of nodes representingall the summits that can be drawn given some constraints
     * in a panorama.
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
                if(x.getSummit().elevation() < y.getSummit().elevation()) {
                    return 1;
                }
                else if(x.getSummit().elevation() > y.getSummit().elevation()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
        
        int width = parameters.width();
        BitSet available = new BitSet(width - 2 * SIDE_BORDER + 2);
        
        
        int height = 0;
        boolean first = true;
        
        for(VisibleSummit s : visibleSummits) {
            
            int X = s.getX();
            int Y = s.getY();
            
            if(X >= SIDE_BORDER 
                    && X <= (width - SIDE_BORDER)
                    && Y >= ABOVE_BORDER
                    && available(available, X - SIDE_BORDER)) {
                
                if(first) {
                    height = Y - LINE_LENGTH;
                    first = false;
                }
                
                labels.add(new Line(X, height, X, Y));
                
                Summit summit = s.getSummit();
                Text text = new Text(0, 0, summit.name() + " (" + summit.elevation() + ")");
                text.getTransforms().addAll(new Translate(X, height), new Rotate(TEXT_ROTATION, 0, 0));
                labels.add(text);
            }
        }

        
        return labels;
    }
    
<<<<<<< HEAD
    /**
     * Indicates if the certain area of a set of bits is avilable
     * @param set the set of bits
     * @param index the begininng of the area
     * 
     * @return true if the area is available
     */
=======
    private static final int SPACE = 20;
    
>>>>>>> 4f9e7fefe1245b7dec98fac0f2c11892429d9841
    private boolean available(BitSet set, int index) {
        
        boolean available = true;
        
        for(int i = index; i < index + SPACE; i++) {
            
            if(set.get(i)) {
                available = false;
            }
        }
        if(available) {
            for(int i = index; i < index + SPACE; i++) {
                set.set(index, true);
            }
        }
        
        return available;
    }
    
    
    private static final int ERROR_CONSTANT = 200;
    private static final int INTERVAL = 64;
    
<<<<<<< HEAD
    /**
     * Construct a list of all the visible summits in a panorama
     * 
     * @param parameters the parameters of the panorama
     * @return the list of all visible summits
     */
    private List<Summit> visibleSummits(PanoramaParameters parameters) {
=======

    private List<VisibleSummit> visibleSummits(PanoramaParameters parameters) {
>>>>>>> 4f9e7fefe1245b7dec98fac0f2c11892429d9841
        
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
                    && abs(angularDistance(parameters.centerAzimuth(), azimuthTo)) <= parameters.horizontalFieldOfView()/2d + 1e-10
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
    
    public static final class VisibleSummit {
        private final Summit summit;
        private final int x;
        private final int y;
        
        public VisibleSummit(Summit s, int x, int y) {
            summit = s;
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public Summit getSummit() {
            return summit;
        }
    }
}
