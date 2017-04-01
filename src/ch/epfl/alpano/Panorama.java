package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;

/**
 * Represent a panorama
 * @author Mathieu Chevalley (274698)
 */
public final class Panorama {

    private final PanoramaParameters panoramaParameters;
    private final float[] distance;
    private final float[] longitude;
    private final float[] latitude;
    private final float[] elevation;
    private final float[] slope;
    
    private Panorama(PanoramaParameters pano, float[] dist, float[] longi, float[] lati, float[] eleva, float[] slo) {
        panoramaParameters = pano;
        distance = dist;
        longitude = longi;
        latitude = lati;
        elevation = eleva;
        slope = slo;
    }
    
    /**
     * 
     * @return the parameters of the panorama
     * @see PanoramaParameters
     */
    public PanoramaParameters parameters() {
        return panoramaParameters;
    }
    
    /**
     * 
     * @param x first index
     * @param y second index
     * @return the corresponding distance
     * @throws IndexOutOfBoundsException if the index is out of the field
     */
    public float distanceAt(int x, int y) {
        checkIndex(x,y);
        return distance[parameters().linearSampleIndex(x, y)];
    }
    
    /**
     * 
     * @param x first index
     * @param y second index
     * @return the corresponding longitude
     * @throws IndexOutOfBoundsException if the index is out of the field
     */
    public float longitudeAt(int x, int y) {
        checkIndex(x,y);
        return longitude[parameters().linearSampleIndex(x, y)];
    }
    
    /**
     * 
     * @param x first index
     * @param y second index
     * @return the corresponding latitude
     * @throws IndexOutOfBoundsException if the index is out of the field
     */
    public float latitudeAt(int x, int y) {
        checkIndex(x,y);
        return latitude[parameters().linearSampleIndex(x, y)];

    }
    
    /**
     * 
     * @param x first index
     * @param y second index
     * @return the corresponding elevation
     * @throws IndexOutOfBoundsException if the index is out of the field
     */
    public float elevationAt(int x, int y) {
        checkIndex(x,y);
        return elevation[parameters().linearSampleIndex(x, y)];

    }
    
    /**
     * 
     * @param x first index
     * @param y second index
     * @return the corresponding slope
     * @throws IndexOutOfBoundsException if the index is out of the field
     */
    public float slopeAt(int x, int y) {
        checkIndex(x,y);
        return slope[parameters().linearSampleIndex(x, y)];

    }
    
    /**
     * 
     * @param x first index
     * @param y second index
     * @param d the default value
     * @return the corresponding distance
     */
    public float distanceAt(int x, int y, float d) {
        if(parameters().isValidSampleIndex(x, y)) {
            return distance[parameters().linearSampleIndex(x, y)];
        }
        else {
            return d;
        }
    }
    
    private void checkIndex(int x, int y) {
        checkArgument(parameters().isValidSampleIndex(x, y));
    }
    
    /**
     * Builder for the panorama class
     * @author Mathieu Chevalley (274698)
     *
     */
    public static final class Builder {
        
        private final PanoramaParameters parameters;
        private final float[] distance;
        private final float[] longitude;
        private final float[] latitude;
        private final float[] elevation;
        private final float[] slope;
        private boolean build = false;
        
        /**
         * Construct the builder
         * @param parameters
         * @throws NullPointerException if the parameters is null
         */
        public Builder(PanoramaParameters parameters) {
            this.parameters = requireNonNull(parameters);
            int length = parameters.height() * parameters.width();
            distance = new float[length];
            Arrays.fill(distance, Float.POSITIVE_INFINITY);
            longitude = new float[length];
            Arrays.fill(longitude, 0);
            latitude = new float[length];
            Arrays.fill(latitude, 0);
            elevation = new float[length];
            Arrays.fill(elevation, 0);
            slope = new float[length];
            Arrays.fill(slope, 0);
        }
        
        /**
         * 
         * @param x first index
         * @param y second index
         * @param distance that corresponds to the index
         * @return this
         * @throws IllegalStateException if already built 
         * @throws IndexOutOfBoundsException if the index are out of the field
         */
        public Builder setDistanceAt(int x, int y, float distance) {
            requireNonBuild();
            checkIndex(x,y);
            this.distance[parameters.linearSampleIndex(x, y)] = distance; 
            return this;
        }
        
        /**
         * 
         * @param x first index
         * @param y second index
         * @param longitude that corresponds to the index
         * @return this
         * @throws IllegalStateException if already built 
         * @throws IndexOutOfBoundsException if the index are out of the field
         */
        public Builder setLongitudeAt(int x, int y, float longitude) {
            requireNonBuild();
            checkIndex(x,y);
            this.longitude[parameters.linearSampleIndex(x, y)] = longitude; 
            return this;
        }
        
        /**
         * 
         * @param x first index
         * @param y second index
         * @param latitude that corresponds to the index
         * @return this
         * @throws IllegalStateException if already built 
         * @throws IndexOutOfBoundsException if the index are out of the field
         */
        public Builder setLatitudeAt(int x, int y, float latitude) {
            requireNonBuild();
            checkIndex(x,y);
            this.latitude[parameters.linearSampleIndex(x, y)] = latitude; 
            return this;
        }
        
        /**
         * 
         * @param x first index
         * @param y second index
         * @param elevation that corresponds to the index
         * @return this
         * @throws IllegalStateException if already built 
         * @throws IndexOutOfBoundsException if the index are out of the field
         */
        public Builder setElevationAt(int x, int y, float elevation) {
            requireNonBuild();
            checkIndex(x,y);
            this.elevation[parameters.linearSampleIndex(x, y)] = elevation; 
            return this;
        }
        
        /**
         * 
         * @param x first index
         * @param y second index
         * @param slope that corresponds to the index
         * @return this
         * @throws IllegalStateException if already built 
         * @throws IndexOutOfBoundsException if the index are out of the field
         */
        public Builder setSlopeAt(int x, int y, float slope) {
            requireNonBuild();
            checkIndex(x,y);
            this.slope[parameters.linearSampleIndex(x, y)] = slope; 
            return this;
        }
        
        /**
         * 
         * @return the panorama
         * @throws IllegalStateException if already built 
         */
        public Panorama build() {
            requireNonBuild();
            build = true;
            return new Panorama(parameters, distance, longitude, latitude, elevation, slope);
        }
        
        private void requireNonBuild() {
            if(build){
                throw new IllegalStateException("already built");
            }
        }
        
        private void checkIndex(int x, int y) {
            if(!parameters.isValidSampleIndex(x, y)) {
                throw new IndexOutOfBoundsException("not in the field");
            }
        }
    }

}
