package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


import java.util.EnumMap;
import java.util.Map;

import static ch.epfl.alpano.gui.UserParameter.*;
import static javafx.application.Platform.runLater;

/**
 * A bean that contains the parameters given by the user
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */
public class PanoramaParametersBean {
    
    private final ObjectProperty<PanoramaUserParameters> parameters;
    private final Map<UserParameter, ObjectProperty<Integer>> objectPropertiesMap;
    
    /**
     * Construct the bean for the panorama parameters given those parameters
     * @param parameters the parameters 
     */
    public PanoramaParametersBean(PanoramaUserParameters parameters) {
        
        this.parameters = new SimpleObjectProperty<>(parameters);
        
        objectPropertiesMap = new EnumMap<>(UserParameter.class);
        for(UserParameter m : UserParameter.values()) {
            objectPropertiesMap.put(m, new SimpleObjectProperty<>(parameters.get(m)));
            
            objectPropertiesMap.get(m).addListener((b, o, n) ->
            runLater(this::synchronizeParameters));
        }
    }
    
    //TODO
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
        return parameters;
    }
    public PanoramaUserParameters getParameters() {
        return parametersProperty().get();
    }
    
    public ObjectProperty<Integer> observerLongitudeProperty() {
        return objectPropertiesMap.get(OBSERVER_LONGITUDE);
    }
    public int getObserverLongitude() {
        return observerLongitudeProperty().get();
    }
    public void setObserverLongitude(int newLongitude) {
        observerLongitudeProperty().set(newLongitude);
    }
    
    public ObjectProperty<Integer> observerLatitudeProperty() {
        return objectPropertiesMap.get(OBSERVER_LATITUDE);
    }
    public int getObserverLatitude() {
        return observerLatitudeProperty().get();
    }
    public void setObserverLatitude(int newLatitude) {
        observerLatitudeProperty().set(newLatitude);
    }
    
    public ObjectProperty<Integer> observerElevationProperty() {
        return objectPropertiesMap.get(OBSERVER_ELEVATION);
    }
    public int getObserverElevation() {
        return observerElevationProperty().get();
    }
    public void setObserverElevation(int newElevation) {
        observerElevationProperty().set(newElevation);
    }
    
    public ObjectProperty<Integer> centerAzimuthProperty() {
        return objectPropertiesMap.get(CENTER_AZIMUTH);
    }
    public int getCenterAzimuth() {
        return centerAzimuthProperty().get();
    }
    public void setCenterAzimuth(int newCenterAzimuth) {
        centerAzimuthProperty().set(newCenterAzimuth);
    }
    
    public ObjectProperty<Integer> horizontalFieldOfViewProperty() {
        return objectPropertiesMap.get(HORIZONTAL_FIELD_OF_VIEW);
    }
    public int getHorizontalFieldOfView() {
        return horizontalFieldOfViewProperty().get();
    }
    public void setHorizontalFieldOfView(int newHorizontalFieldOfView) {
        horizontalFieldOfViewProperty().set(newHorizontalFieldOfView);
    }
    
    public ObjectProperty<Integer> maxDistanceProperty() {
        return objectPropertiesMap.get(MAX_DISTANCE);
    }
    public int getMaxDistance() {
        return maxDistanceProperty().get();
    }
    public void setMaxDistance(int newMaxDistance) {
        maxDistanceProperty().set(newMaxDistance);
    }
    
    public ObjectProperty<Integer> widthProperty() {
        return objectPropertiesMap.get(WIDTH);
    }
    public int getWidth() {
        return widthProperty().get();
    }
    public void setWidth(int newWidth) {
        widthProperty().set(newWidth);
    }
    
    public ObjectProperty<Integer> heightProperty() {
        return objectPropertiesMap.get(HEIGHT);
    }
    public int getHeight() {
        return heightProperty().get();
    }
    public void setHeight(int newHeight) {
        heightProperty().set(newHeight);
    }
    
    public ObjectProperty<Integer> superSamplingExponentProperty() {
        return objectPropertiesMap.get(SUPER_SAMPLING_EXPONENT);
    }
    public int getSuperSamplingExponent() {
        return superSamplingExponentProperty().get();
    }
    public void setSuperSamplingExponent(int newSuperSamplingExponent) {
        superSamplingExponentProperty().set(newSuperSamplingExponent);
    }
    
    //update all the parameters when the parameters changed
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
