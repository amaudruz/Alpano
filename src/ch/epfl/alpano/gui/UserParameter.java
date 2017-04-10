package ch.epfl.alpano.gui;

public enum UserParameter {
    
    OBSERVER_LONGITUDE(60_000, 120_000),
    OBSERVER_LATITUDE(470_000, 480_000),
    OBSERVER_ELEVATION(300, 10000),
    CENTER_AZIMUTH(0, 359),
    HORIZONTAL_FIELD_OF_VIEW(1, 360),
    MAX_DISTANCE(10, 600),
    WIDTH(30, 16_000),
    HEIGHT(10, 4000),
    SUPER_SAMPLING_EXPONENT(0, 2);
    
    private final int maxValue;
    private final int minValue;
    
    private UserParameter(int min, int max) {
        minValue = min;
        maxValue = max;
    }
    
    public int sanitize(int value) {
        
        if(value <= minValue) {
            return minValue;
        }
        else if(value >= maxValue) {
            return maxValue;
        }
        else{
            return value;
        }
        
    }
}
