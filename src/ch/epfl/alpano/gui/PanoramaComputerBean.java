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


public final class PanoramaComputerBean {
    
    private final ObjectProperty<PanoramaUserParameters> parameters;
    private final ObjectProperty<Image> image;
    private final ObjectProperty<ObservableList<Node>> labels;
    private final ObjectProperty<Panorama> panorama;
    
    private Labelizer labelizer;
    private ContinuousElevationModel dem;
    
    public PanoramaComputerBean(PanoramaUserParameters parameters, List<Summit> summits, ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
        labelizer = new Labelizer(dem, summits);
        
        this.parameters = new SimpleObjectProperty<>(parameters);
        labels = new SimpleObjectProperty<>(computeLabels(parameters.panoramaParameters(), labelizer));
        panorama = new SimpleObjectProperty<>(computePanorama(dem, parameters.panoramaParameters()));
        image = new SimpleObjectProperty<>(computeImage(getPanorama()));
        
        this.parameters.addListener((b, o, n) ->
            runLater(this::compute));
        
    }
    
    private void compute() {
        
        PanoramaParameters parameters = getParameters().panoramaParameters();
        panorama.set(computePanorama(dem, parameters));
        
        labels.set(computeLabels(parameters, labelizer));
        image.set(computeImage(getPanorama()));
    }
    
    private Panorama computePanorama(ContinuousElevationModel dem,
            PanoramaParameters panoramaParameters) {
        
        PanoramaComputer computer = new PanoramaComputer(dem);
        
        return computer.computePanorama(panoramaParameters);
    }

    private ObservableList<Node> computeLabels(PanoramaParameters panoramaParameters, Labelizer labelizer) {
        return   FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(labelizer.labels(panoramaParameters)));
    }

    private Image computeImage(Panorama panorama) {
        ChannelPainter dist = panorama::distanceAt;
        ChannelPainter hue = dist.div(100000).cycle().mul(360);
        ChannelPainter s = dist.div(200000).clamp().invert();
        ChannelPainter slo = panorama::slopeAt;
        ChannelPainter b = slo.mul(2).div((float) Math.PI).invert().mul(0.7f).add(0.3f);
        ChannelPainter o = (x,y) -> dist.valueAt(x, y) == Float.POSITIVE_INFINITY ? 0 : 1; 
           
        ImagePainter painter = ImagePainter.hsb(hue, s, b, o);
        return PanoramaRenderer.renderPanorama(panorama, painter);
    }

    /*private ContinuousElevationModel computeDem() {
        int longitude = getParameters().get(UserParameter.OBSERVER_LONGITUDE) / 10000;
        int latitude = getParameters().get(UserParameter.OBSERVER_LATITUDE) / 10000;
        String lon = "";
        
        if(longitude < 10) {
            lon = "00" + latitude;
        }
        else if(longitude < 100) {
            lon = "0" + latitude;
        }
        String fileName = "N" + latitude + "E" + lon + ".hgt";
        File file = new File(fileName);
        return new ContinuousElevationModel(new HgtDiscreteElevationModel(file));
    }*/

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
    

    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty() {
        return labels;
    }
    public ObservableList<Node> getLabels() {
        return labelsProperty().get();
    }
    
    


}
