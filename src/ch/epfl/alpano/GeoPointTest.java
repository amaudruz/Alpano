package ch.epfl.alpano;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class GeoPointTest {

    @Test(expected = IllegalArgumentException.class)
    public void builderErrorOnWrongLatitude(){
        GeoPoint p = new GeoPoint(0, 8);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void builderErrorOnWrongLongitude(){
        GeoPoint p = new GeoPoint(8,0);
    }
    
    @Test
    public void distanceToOnTrivial(){
        GeoPoint p = new GeoPoint(0.11573,0.81194);
        GeoPoint p2 = new GeoPoint(0.65665, 0.97307);
        assertEquals(2370000, p.distanceTo(p2), 100);
    }
    
    @Test
    public void azimuthToOnTrivial(){
        GeoPoint p = new GeoPoint(0.11573,0.81194);
        GeoPoint p2 = new GeoPoint(0.65665, 0.97307);
        System.out.println(p2);
        assertEquals(-52.95, p.azimuthTo(p2)*180/Math.PI, 0.01);
    }
    
    
    
}
