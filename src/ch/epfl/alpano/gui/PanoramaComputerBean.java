package ch.epfl.alpano.gui;

import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import static java.util.Objects.requireNonNull;

/**
 * A bean that contains the proprieties of the panorama (image, labels...)
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */

public final class PanoramaComputerBean {
    
    private final ObjectProperty<PanoramaUserParameters> parameters;
    private final ObjectProperty<Image> image;
    private final ObjectProperty<Panorama> panorama;
    
    private final ObservableList<Node> labels;
    private final ObservableList<Node> unmodifiableLabels;
    private final Labelizer labelizer;
    private final ContinuousElevationModel dem;
     
     /**
     * Construct a panorama computer bean given all the summits and a continuous elevation model
     * @param summits all the summits 
     * @param dem the continuous elevation model
     */
    public PanoramaComputerBean(List<Summit> summits, ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);//TODO
        labelizer = new Labelizer(dem, summits);
        

        parameters = new SimpleObjectProperty<>();
        panorama = new SimpleObjectProperty<>();
        image = new SimpleObjectProperty<>();
        labels = FXCollections.observableArrayList();
        unmodifiableLabels = FXCollections.unmodifiableObservableList(labels);
        
        parameters.addListener((b, oldParameters, newParameters) -> compute(newParameters));//TODO
        
    }
    
    //Compute all the values with the new parameters
    private void compute(PanoramaUserParameters n) {
        
        PanoramaParameters parameters = n.panoramaParameters();
        
        panorama.set(computePanorama(dem, parameters));
        
        computeLabels(n.panoramaDisplayParameters());
        
        image.set(computeImage(getPanorama()));
    }
    
    //compute the new panorama
    private Panorama computePanorama(ContinuousElevationModel dem,
            PanoramaParameters panoramaParameters) {
        
        PanoramaComputer computer = new PanoramaComputer(dem);    
        return computer.computePanorama(panoramaParameters);
    }

    //update the labels
    private void computeLabels(PanoramaParameters panoramaParameters) {
        labels.setAll(labelizer.labels(panoramaParameters));
    }

    //compute the new image
    private Image computeImage(Panorama panorama) {
        //TODO
        ChannelPainter dist = panorama::distanceAt;
        ChannelPainter hue = dist.div(100_000).cycle().mul(360);
        ChannelPainter s = dist.div(200_000).clamp().invert();
        
        ChannelPainter slo = panorama::slopeAt;
        ChannelPainter b = slo.mul(2).div((float) Math.PI).invert().mul(0.7f).add(0.3f);
        ChannelPainter o = (x,y) -> dist.valueAt(x, y) == Float.POSITIVE_INFINITY ? 0 : 1; 
           
        ImagePainter painter = ImagePainter.hsb(hue, s, b, o);
        return PanoramaRenderer.renderPanorama(panorama, painter);
    }

    
    public ObjectProperty<PanoramaUserParameters> parametersProperty() {
        return parameters;
    }
    public PanoramaUserParameters getParameters() {
        return parametersProperty().get();
    }
    public void setParameters(PanoramaUserParameters newParameters) {
        parametersProperty().set(newParameters);
    }
    
    public ReadOnlyObjectProperty<Panorama> panoramaProperty() {
        return panorama;
    }
    public Panorama getPanorama() {
        return panoramaProperty().get();
    }
    
    public ReadOnlyObjectProperty<Image> imageProperty() {
        return image;
    }
    public Image getImage() {
        return imageProperty().get();
    }
   
    public ObservableList<Node> getLabels() {
        return unmodifiableLabels;
    }
    

}
