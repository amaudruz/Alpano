package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;


import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

import static ch.epfl.alpano.gui.UserParameter.*;
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;


/**
 * The parameters given by the user for the panorama
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */
public final class PanoramaUserParameters {
    
    private final Map<UserParameter, Integer> userParameters;
    
    /**
     * Construct a panorama given the user parameters
     * @param parameters a map of the user parameters with their value
     * @see UserParameter
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> parameters) {
        requireNonNull(parameters);
        for(UserParameter m : parameters.keySet()) {
            parameters.put(m, m.sanitize(parameters.get(m)));
        }
        
        int maxHeight = (170 * (parameters.get(WIDTH) - 1) / parameters.get(HORIZONTAL_FIELD_OF_VIEW)) + 1;
        
        if(parameters.get(HEIGHT) > maxHeight) {
            parameters.put(HEIGHT, maxHeight);
        }
        
        userParameters = Collections.unmodifiableMap(new EnumMap<>(parameters));
    }
    
    /**
     * Construct a panorama given the user parameters
     * @param longitude longitude of the observer
     * @param latitude latitude of the observer
     * @param elevation elevation of the observer
     * @param azimuth central azimuth of the view
     * @param horizontalField horizontal field of view
     * @param maxDistance maximum distance that can be seen
     * @param width width of the image
     * @param height height of the image
     * @param superSamplingExponent exponent giving the amount of super sampling
     */
    public PanoramaUserParameters(int longitude, int latitude, int elevation, int azimuth, int horizontalField, 
            int maxDistance, int width, int height, int superSamplingExponent) {
        
        this(createMap(longitude, latitude, elevation, azimuth, horizontalField, maxDistance, width, height, superSamplingExponent));
    }
    
    private static Map<UserParameter,Integer> createMap(int longitude, int latitude, int elevation, int azimuth, int horizontalField, 
            int maxDistance, int width, int height, int superSamplingExponent) {
        Map<UserParameter, Integer> userParameters = new EnumMap<>(UserParameter.class);
        userParameters.put(OBSERVER_LONGITUDE, longitude);
        userParameters.put(OBSERVER_LATITUDE, latitude);
        userParameters.put(OBSERVER_ELEVATION, elevation);
        userParameters.put(CENTER_AZIMUTH, azimuth);
        userParameters.put(HORIZONTAL_FIELD_OF_VIEW, horizontalField);
        userParameters.put(MAX_DISTANCE, maxDistance);
        userParameters.put(WIDTH, width);
        userParameters.put(HEIGHT, height);
        userParameters.put(SUPER_SAMPLING_EXPONENT, superSamplingExponent);
        
        return userParameters;
    }
    
    public PanoramaParameters panoramaParameters() {
        return new PanoramaParameters(new GeoPoint(toRadians(observerLongitude()/10000d), toRadians(observerLatitude()/10000d)), observerElevation(), toRadians(azimuth()), toRadians(HorizontalFieldOfView()), maxDistance() * 1000, (int) Math.pow(2, superSamplingExponent()) * width(), (int) Math.pow(2, superSamplingExponent()) * height());
    }
    
    public PanoramaParameters panoramaDisplayParameters() {
        return new PanoramaParameters(new GeoPoint(toRadians(observerLongitude()/10000d), toRadians(observerLatitude()/10000d)), observerElevation(), toRadians(azimuth()), toRadians(HorizontalFieldOfView()), maxDistance() * 1000, width(), height());
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof PanoramaUserParameters && userParameters.equals(((PanoramaUserParameters)other).userParameters);
    }
    
    @Override
    public int hashCode() {
        return userParameters.hashCode();
    }
    
    public int get(UserParameter parameter) {
        return userParameters.get(parameter);
    }
    
    public int observerLongitude() {
        return get(OBSERVER_LONGITUDE);
    }
    
    public int observerLatitude() {
        return get(OBSERVER_LATITUDE);
    }
    
    public int observerElevation() {
        return get(OBSERVER_ELEVATION);
    }
    
    public int azimuth() {
        return get(CENTER_AZIMUTH);
    }
    
    public int HorizontalFieldOfView() {
        return get(HORIZONTAL_FIELD_OF_VIEW);
    }
    
    public int maxDistance() {
        return get(MAX_DISTANCE);
    }
    
    public int width() {
        return get(WIDTH);
    }
    
    public int height() {
        return get(HEIGHT);
    }
    
    public int superSamplingExponent() {
        return get(SUPER_SAMPLING_EXPONENT);
    }
}
