package ch.epfl.alpano.gui;

import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import static java.util.Objects.requireNonNull;
import static javafx.application.Platform.runLater;
import static ch.epfl.alpano.gui.UserParameter.*;

import java.io.File;

public final class PanoramaComputerBean {
    
    private final ObjectProperty<PanoramaUserParameters> parameters;
    private final ObjectProperty<Image> image;
    private final ObjectProperty<ObservableList<Node>> labels;
    private final ObjectProperty<Panorama> panorama;
    
    private Labelizer labelizer;
    private ContinuousElevationModel dem;
    private final ImagePainter painter;
    private final List<Summit> summits;
    
    public PanoramaComputerBean(PanoramaUserParameters parameters, List<Summit> summits, ImagePainter painter, ContinuousElevationModel dem) {
        this.painter = requireNonNull(painter);
        this.summits = requireNonNull(summits);
        this.dem = requireNonNull(dem);
        labelizer = new Labelizer(dem, summits);
        
        this.parameters = new SimpleObjectProperty<>(parameters);
        labels = new SimpleObjectProperty<>(computeLabels(parameters.panoramaParameters(), labelizer));
        panorama = new SimpleObjectProperty<>(computePanorama(dem, parameters.panoramaParameters()));
        image = new SimpleObjectProperty<>(computeImage(painter, getPanorama()));
        
        this.parameters.addListener((b, o, n) ->
            runLater(this::compute));
        
    }
    
    private void compute() {
        
        PanoramaParameters parameters = getParameters().panoramaParameters();
        panorama.set(computePanorama(dem, parameters));
        
        labels.set(computeLabels(parameters, labelizer));
        image.set(computeImage(painter, getPanorama()));
    }
    
    private Panorama computePanorama(ContinuousElevationModel dem,
            PanoramaParameters panoramaParameters) {
        
        PanoramaComputer computer = new PanoramaComputer(dem);
        
        return computer.computePanorama(panoramaParameters);
    }

    private ObservableList<Node> computeLabels(PanoramaParameters panoramaParameters, Labelizer labelizer) {
        return   FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(labelizer.labels(panoramaParameters)));
    }

    private Image computeImage(ImagePainter painter,
            Panorama panorama) {
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
