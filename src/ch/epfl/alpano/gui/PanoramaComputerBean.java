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
import static javafx.application.Platform.runLater;

/**
 * A bean that contains the proprieties of the panorama (image, labels...)
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */

public final class PanoramaComputerBean {
    
    private final ObjectProperty<PanoramaUserParameters> parameters;
    private final ObjectProperty<Image> image;
    private final ObjectProperty<ObservableList<Node>> labels;
    private final ObjectProperty<Panorama> panorama;
    
    private Labelizer labelizer;
    private ContinuousElevationModel dem;
    private final ObservableList<Node> unmodifiableList;
     
     /**
     * Construct a panorama computer bean given all the summits and a continious elevation model
     * @param summits all the summits 
     * @param dem the continious elevation model
     */

    public PanoramaComputerBean(List<Summit> summits, ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
        labelizer = new Labelizer(dem, summits);
        
        /*this.parameters = new SimpleObjectProperty<>(parameters);
        labels = new SimpleObjectProperty<>(FXCollections.observableArrayList(labelizer.labels(parameters.panoramaParameters())));
        panorama = new SimpleObjectProperty<>(computePanorama(dem, parameters.panoramaParameters()));
        image = new SimpleObjectProperty<>(computeImage(getPanorama()));
        unmodifiableList = FXCollections.unmodifiableObservableList(labelsProperty().get());*/
        this.parameters = new SimpleObjectProperty<>();
        labels = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        panorama = new SimpleObjectProperty<>();
        image = new SimpleObjectProperty<>();
        unmodifiableList = FXCollections.unmodifiableObservableList(labelsProperty().get());
        
        this.parameters.addListener((b, o, n) ->
            compute(n));
        
    }
    
    private void compute(PanoramaUserParameters n) {
        
        PanoramaParameters parameters = n.panoramaParameters();
        panorama.set(computePanorama(dem, parameters));
        
        computeLabels(n.panoramaDisplayParameters());
        image.set(computeImage(getPanorama()));
    }
    
    private Panorama computePanorama(ContinuousElevationModel dem,
            PanoramaParameters panoramaParameters) {
        
        PanoramaComputer computer = new PanoramaComputer(dem);
        
        return computer.computePanorama(panoramaParameters);
    }

    private void computeLabels(PanoramaParameters panoramaParameters) {
        labels.get().setAll(labelizer.labels(panoramaParameters));
    }

    private Image computeImage(Panorama panorama) {
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
        return unmodifiableList;
    }
    
    


}
