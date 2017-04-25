package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
    
    public List<Node> labels(PanoramaParameters parameters) {
        
        List<Node> labels = new ArrayList<>();
        List<Summit> visibleSummits = visibleSummits(parameters);
        
        Collections.sort(visibleSummits, (x, y) -> {
            if(x.elevation() < y.elevation()) {
                return 1;
            }
            else if (x.elevation() == y.elevation()) {
                return 0;
            }
            else{
                return -1;
            }
        });
        
        BitSet available = new BitSet(parameters.width() - 40);
        
        int height = 0;
        boolean first = true;
        
        for(Summit s : visibleSummits) {
            double azimuth = parameters.observerPosition().azimuthTo(s.position());
            double altitude = tan((s.elevation() - parameters.observerElevation())/parameters.observerPosition().distanceTo(s.position()));
            
            if(parameters.xForAzimuth(azimuth) >= 20 &&  parameters.xForAzimuth(azimuth) <= parameters.width() - 20
                    && parameters.yForAltitude(altitude) <= parameters.height() - 170
                    && available(available, (int) parameters.xForAzimuth(azimuth))) {
                
                if(first) {
                    height = (int) parameters.yForAltitude(altitude) + 22;
                    first = false;
                }
                
                Line line = new Line((int) parameters.xForAzimuth(azimuth), (int) parameters.yForAltitude(altitude), (int) parameters.xForAzimuth(azimuth), height - 2);
                labels.add(line);
                
                Text text = new Text((int) parameters.xForAzimuth(azimuth), height, s.name());
                text.getTransforms().add(new Rotate(60,0,0));
                labels.add(text);
            }
        }

        
        return labels;
    }
    
    private boolean available(BitSet set, int index) {
        boolean available = true;
        for(int i = index; i < index + 20; i++) {
            if(!set.get(i)) {
                available = false;
            }
        }
        
        return available;
    }
    
    private List<Summit> visibleSummits(PanoramaParameters parameters) {
        List<Summit> visibleSummits = new LinkedList<>();
        
        for(Summit s : summits) {
            ElevationProfile profile = new ElevationProfile(cem, parameters.observerPosition(), parameters.observerPosition().azimuthTo(s.position()), parameters.observerPosition().distanceTo(s.position()));
            if(parameters.observerPosition().distanceTo(s.position()) <= parameters.maxDistance()
                    && abs(angularDistance(parameters.observerPosition().azimuthTo(s.position()) , parameters.centerAzimuth())) <= parameters.horizontalFieldOfView()/2 + 1e-10
                    && abs(tan((s.elevation() - parameters.observerElevation())/parameters.observerPosition().distanceTo(s.position()))) <= parameters.verticalFieldOfView()/2
                    && Math2.firstIntervalContainingRoot(PanoramaComputer.rayToGroundDistance(profile, parameters.observerElevation(), (s.elevation() - parameters.observerElevation())/parameters.observerPosition().distanceTo(s.position())), 0, parameters.observerPosition().distanceTo(s.position()) - 200, 64) == Double.POSITIVE_INFINITY) {
                
                visibleSummits.add(s);
            }
        }
        
        return visibleSummits;
    }
}
