package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


import java.util.EnumMap;
import java.util.Map;

import static ch.epfl.alpano.gui.UserParameter.*;
import static javafx.application.Platform.runLater;

public class PanoramaParametersBean {
    
    private final ObjectProperty<PanoramaUserParameters> parameters;
    private final Map<UserParameter, ObjectProperty<Integer>> objectPropertiesMap;

    public PanoramaParametersBean(PanoramaUserParameters parameters) {
        this.parameters = new SimpleObjectProperty<>(parameters);
        objectPropertiesMap = new EnumMap<>(UserParameter.class);
        objectPropertiesMap.put(OBSERVER_LONGITUDE, new SimpleObjectProperty<>(parameters.get(OBSERVER_LONGITUDE)));
        objectPropertiesMap.put(OBSERVER_LATITUDE, new SimpleObjectProperty<>(parameters.get(OBSERVER_LATITUDE)));
        objectPropertiesMap.put(OBSERVER_ELEVATION, new SimpleObjectProperty<>(parameters.get(OBSERVER_ELEVATION)));
        objectPropertiesMap.put(CENTER_AZIMUTH, new SimpleObjectProperty<>(parameters.get(CENTER_AZIMUTH)));
        objectPropertiesMap.put(HORIZONTAL_FIELD_OF_VIEW, new SimpleObjectProperty<>(parameters.get(HORIZONTAL_FIELD_OF_VIEW)));
        objectPropertiesMap.put(MAX_DISTANCE, new SimpleObjectProperty<>(parameters.get(MAX_DISTANCE)));
        objectPropertiesMap.put(WIDTH, new SimpleObjectProperty<>(parameters.get(WIDTH)));
        objectPropertiesMap.put(HEIGHT, new SimpleObjectProperty<>(parameters.get(HEIGHT)));
        objectPropertiesMap.put(SUPER_SAMPLING_EXPONENT, new SimpleObjectProperty<>(parameters.get(SUPER_SAMPLING_EXPONENT)));
        for(UserParameter m : objectPropertiesMap.keySet()) {
            objectPropertiesMap.get(m).addListener((b, o, n) ->
            runLater(this::synchronizeParameters));
        }
    }
    
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
        return parameters;
    }
    public PanoramaUserParameters getParameters() {
        return parameters.get();
    }
    
    public ObjectProperty<Integer> observerLongitudeProperty() {
        return objectPropertiesMap.get(OBSERVER_LONGITUDE);
    }
    public int getObserverLongitude() {
        return objectPropertiesMap.get(OBSERVER_LONGITUDE).get();
    }
    public void setObserverLongitude(int newLongitude) {
        objectPropertiesMap.get(OBSERVER_LONGITUDE).set(newLongitude);
    }
    
    public ObjectProperty<Integer> observerLatitudeProperty() {
        return objectPropertiesMap.get(UserParameter.OBSERVER_LATITUDE);
    }
    public int getObserverLatitude() {
        return objectPropertiesMap.get(UserParameter.OBSERVER_LATITUDE).get();
    }
    public void setObserverLatitude(int newLatitude) {
        objectPropertiesMap.get(UserParameter.OBSERVER_LATITUDE).set(newLatitude);
    }
    
    public ObjectProperty<Integer> observerElevationProperty() {
        return objectPropertiesMap.get(UserParameter.OBSERVER_ELEVATION);
    }
    public int getObserverElevation() {
        return objectPropertiesMap.get(UserParameter.OBSERVER_ELEVATION).get();
    }
    public void setObserverElevation(int newElevation) {
        objectPropertiesMap.get(UserParameter.OBSERVER_ELEVATION).set(newElevation);
    }
    public ObjectProperty<Integer> centerAzimuthProperty() {
        return objectPropertiesMap.get(UserParameter.CENTER_AZIMUTH);
    }
    public int getCenterAzimuth() {
        return objectPropertiesMap.get(UserParameter.CENTER_AZIMUTH).get();
    }
    public void setCenterAzimuth(int newCenterAzimuth) {
        objectPropertiesMap.get(UserParameter.CENTER_AZIMUTH).set(newCenterAzimuth);
    }
    public ObjectProperty<Integer> horizontalFieldOfViewProperty() {
        return objectPropertiesMap.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }
    public int getHorizontalFieldOfView() {
        return objectPropertiesMap.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW).get();
    }
    public void setHorizontalFieldOfView(int newHorizontalFieldOfView) {
        objectPropertiesMap.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW).set(newHorizontalFieldOfView);
    }
    public ObjectProperty<Integer> maxDistanceProperty() {
        return objectPropertiesMap.get(UserParameter.MAX_DISTANCE);
    }
    public int getMaxDistance() {
        return objectPropertiesMap.get(UserParameter.MAX_DISTANCE).get();
    }
    public void setMaxDistance(int newMaxDistance) {
        objectPropertiesMap.get(UserParameter.MAX_DISTANCE).set(newMaxDistance);
    }
    public ObjectProperty<Integer> widthProperty() {
        return objectPropertiesMap.get(UserParameter.WIDTH);
    }
    public int getWidth() {
        return objectPropertiesMap.get(UserParameter.WIDTH).get();
    }
    public void setWidth(int newWidth) {
        objectPropertiesMap.get(UserParameter.WIDTH).set(newWidth);
    }
    public ObjectProperty<Integer> heightProperty() {
        return objectPropertiesMap.get(UserParameter.HEIGHT);
    }
    public int getHeight() {
        return objectPropertiesMap.get(UserParameter.HEIGHT).get();
    }
    public void setHeight(int newHeight) {
        objectPropertiesMap.get(UserParameter.HEIGHT).set(newHeight);
    }
    public ObjectProperty<Integer> superSamplingExponentProperty() {
        return objectPropertiesMap.get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }
    public int getSuperSamplingExponent() {
        return objectPropertiesMap.get(UserParameter.SUPER_SAMPLING_EXPONENT).get();
    }
    public void setSuperSamplingExponent(int newSuperSamplingExponent) {
        objectPropertiesMap.get(UserParameter.SUPER_SAMPLING_EXPONENT).set(newSuperSamplingExponent);
    }
    
    private void synchronizeParameters() {
        Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
        for(UserParameter m : objectPropertiesMap.keySet()) {
            map.put(m, objectPropertiesMap.get(m).get());
        }
        PanoramaUserParameters parameters = new PanoramaUserParameters(map);
        this.parameters.set(parameters);
        for(UserParameter m : objectPropertiesMap.keySet()) {
            objectPropertiesMap.get(m).set(parameters.get(m));
        }
    }

}
