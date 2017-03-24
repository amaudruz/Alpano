package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;

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
    
    public PanoramaParameters parameters(){
        return panoramaParameters;
    }
    

    public float distanceAt(int x, int y) {
        checkIndex(x,y);
        return distance[parameters().linearSampleIndex(x, y)];
    }
    public float longitudeAt(int x, int y) {
        checkIndex(x,y);
        return longitude[parameters().linearSampleIndex(x, y)];
    }
    public float latitudeAt(int x, int y) {
        checkIndex(x,y);
        return latitude[parameters().linearSampleIndex(x, y)];

    }
    public float elevationAt(int x, int y) {
        checkIndex(x,y);
        return elevation[parameters().linearSampleIndex(x, y)];

    }
    public float slopeAt(int x, int y) {
        checkIndex(x,y);
        return slope[parameters().linearSampleIndex(x, y)];

    }
    
    public float distanceAt(int x, int y, float d){
        if(parameters().isValidSampleIndex(x, y)){
            return distance[parameters().linearSampleIndex(x, y)];
        }
        else{
            return d;
        }
    }
    
    private void checkIndex(int x, int y){
        if(!parameters().isValidSampleIndex(x, y)){
            throw new IndexOutOfBoundsException();
        }
    }
    
    public static final class Builder {
        
        private final PanoramaParameters parameters;
        private final float[] distance;
        private final float[] longitude;
        private final float[] latitude;
        private final float[] elevation;
        private final float[] slope;
        private boolean build = false;
        
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
        
        public Builder setDistanceAt(int x, int y, float distance) {
            requireNonBuild();
            checkIndex(x,y);
            this.distance[parameters.linearSampleIndex(x, y)] = distance; 
            return this;
        }
        public Builder setLongitudeAt(int x, int y, float longitude) {
            requireNonBuild();
            checkIndex(x,y);
            this.longitude[parameters.linearSampleIndex(x, y)] = longitude; 
            return this;
        }
        public Builder setLatitudeAt(int x, int y, float latitude) {
            requireNonBuild();
            checkIndex(x,y);
            this.latitude[parameters.linearSampleIndex(x, y)] = latitude; 
            return this;
        }
        public Builder setElevationAt(int x, int y, float elevation) {
            requireNonBuild();
            checkIndex(x,y);
            this.elevation[parameters.linearSampleIndex(x, y)] = elevation; 
            return this;
        }
        public Builder setSlopeAt(int x, int y, float slope) {
            requireNonBuild();
            checkIndex(x,y);
            this.slope[parameters.linearSampleIndex(x, y)] = slope; 
            return this;
        }
        
        public Panorama build() {
            requireNonBuild();
            return new Panorama(parameters, distance, longitude, latitude, elevation, slope);
        }
        
        private void requireNonBuild() {
            if(build){
                throw new IllegalStateException("already built");
            }
        }
        
        private void checkIndex(int x, int y){
            if(!parameters.isValidSampleIndex(x, y)){
                throw new IndexOutOfBoundsException();
            }
        }
    }

}
