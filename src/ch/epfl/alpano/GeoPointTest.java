package ch.epfl.alpano;

import static org.junit.Assert.assertEquals;
import static ch.epfl.alpano.Azimuth.*;

import org.junit.Test;
import ch.epfl.alpano.Distance.*;

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
        GeoPoint p3 = new GeoPoint(0.11573, 1);
        GeoPoint Eiger = new GeoPoint( 8.00534 * Math.PI/180,46.57761 * Math.PI/180);
        GeoPoint LC = new GeoPoint( 6.56727* Math.PI/180,46.51781* Math.PI/180);
        
        assertEquals(2370000, p.distanceTo(p2), 10000);
        assertEquals(0, p.distanceTo(p), 0);
        assertEquals((1-0.81194)*Distance.EARTH_RADIUS,p.distanceTo(p3), 100 );
        assertEquals(110490, LC.distanceTo(Eiger), 100);

        

    }
    
    @Test
    public void azimuthToOnTrivial(){
        GeoPoint p = new GeoPoint(0.11573,0.81194);
        GeoPoint p2 = new GeoPoint(0.65665, 0.97307);
        GeoPoint p3 = new GeoPoint(0,0.81194);
        GeoPoint p4 = new GeoPoint(0.11573, 0);
        GeoPoint Eiger = new GeoPoint( 8.00534 * Math.PI/180,46.57761 * Math.PI/180);
        GeoPoint LC = new GeoPoint( 6.56727* Math.PI/180,46.51781* Math.PI/180);
        
        assertEquals(52.95, p.azimuthTo(p2)*180/Math.PI, 0.01);
        assertEquals(0, p.azimuthTo(p), 0.01);
        assertEquals(3*Math.PI/2, p.azimuthTo(p3), 0.1);
        assertEquals(Math.PI, p.azimuthTo(p4), 0.01);
        assertEquals(86.67*Math.PI/180, LC.azimuthTo(Eiger), 0.01);

    }
    
   
    
    @Test 
    public void ToStringTest() {
        GeoPoint p = new GeoPoint(0.11573,0.81194);
        assertEquals("[6.631,46.521]", p.toString(),0);
        
    }
    
    
    
}
