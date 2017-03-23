package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class PanoramaParametersTest {
	
	private final GeoPoint origin = new GeoPoint(0,0);
	private final double centerAzi = 0;
	private final double horizontalvue = Math.PI/3;
	private final int width = 40;
	private final int height = 20;
	
	
	@Test(expected = IllegalArgumentException.class)
	public void ExceptionsTestBuilderAzimut() {
		PanoramaParameters james = new PanoramaParameters(origin, 
				0, -1, this.horizontalvue, 1000, this.width, this.height);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ExceptionsTestazimutForX() {
		PanoramaParameters james1 = new PanoramaParameters(origin, 
				0, 1, this.horizontalvue, 1000, this.width, this.height);
		james1.azimuthForX(40);
	
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ExceptionsTestXForazimuth() {
		PanoramaParameters james1 = new PanoramaParameters(origin, 
				0, 0, this.horizontalvue, 1000, this.width, this.height);
		james1.xForAzimuth((20 * Math.PI/11 + 0.001));
	
	}
	@Test(expected = IllegalArgumentException.class)
	public void ExceptionsTestXForazimuth2() {
		PanoramaParameters james1 = new PanoramaParameters(origin, 
				0, Math.PI/6, this.horizontalvue, 1000, this.width, this.height);
		james1.xForAzimuth((6.28));
	
	}
	
	@Test
	public void TestXForazimuth() {
		PanoramaParameters james1 = new PanoramaParameters(origin, 
				0, 0, this.horizontalvue, 1000, this.width, this.height);
		assertEquals((this.width-1)/2.0, james1.xForAzimuth(this.centerAzi), 0.000001);
		assertEquals(0,james1.xForAzimuth((11*Math.PI/6.0) + 0.000000000000014), 0.00001);
		assertEquals(this.width-1,james1.xForAzimuth((Math.PI/6.0) ), 0.00001);
		    //assertEquals(0, james1.xForAzimuth(james1.azimuthForX(0)),1e-10);
		for(int i = 0; i < width; i++){
		    assertEquals(i, james1.xForAzimuth(james1.azimuthForX(i)), 1e-10);
		}

	}
	
	@Test
	public void TestazimuthForX() {
		PanoramaParameters james1 = new PanoramaParameters(origin, 
				0, 0, this.horizontalvue, 1000, this.width, this.height);
		
		assertEquals(this.centerAzi, james1.azimuthForX((this.width-1)/2.0), 0.000001);
		assertEquals((11*Math.PI/6.0) , james1.azimuthForX(0), 0.000001);
		assertEquals(Math.PI/6, james1.azimuthForX(this.width-1), 0.00001);


	}
	
	@Test
	public void TestYForaltitude() {
		PanoramaParameters james1 = new PanoramaParameters(origin, 
				0, 0, this.horizontalvue, 1000, this.width, this.height);
		assertEquals((this.height-1)/2.0, james1.yForAltitude(0), 0.000001);
		assertEquals((this.height-1), james1.yForAltitude(-((this.height-1)/2.0)*james1.anglePerPixels()), 0.00001);
		assertEquals(0, james1.yForAltitude(((this.height-1)/2.0)*james1.anglePerPixels()), 0.00001);
		assertEquals(5, james1.yForAltitude(james1.altitudeForY(5)),0);
		for(int i = 0; i < height; i++){
		    assertEquals(i, james1.yForAltitude(james1.altitudeForY(i)), 1e-10);
		}
	}
	
	
	@Test
	public void TestaltitudeForY() {
		PanoramaParameters james1 = new PanoramaParameters(origin, 
				0, 0, this.horizontalvue, 1000, this.width, this.height);
		System.out.println((this.height-1)/2.0 + " " + (james1.height()-1)/2.0);
		assertEquals(0 , james1.altitudeForY((this.height-1)/2.0), 0.00001 );
		assertEquals(-((this.height-1)/2.0)*james1.anglePerPixels() , james1.altitudeForY((this.height-1)), 0.00001 );
		assertEquals(((this.height-1)/2.0)*james1.anglePerPixels() , james1.altitudeForY(0), 0.00001 );

	}
	
	@Test
	public void TestisValidIndex() {
		
	PanoramaParameters james1 = new PanoramaParameters(origin, 
			0, 0, this.horizontalvue, 1000, this.width, this.height);
	assertTrue(!james1.isValidSampleIndex(this.width, this.height));
	assertTrue(!james1.isValidSampleIndex(-1, this.height-1));
	assertTrue(!james1.isValidSampleIndex(this.width-1, -1));
	assertTrue(james1.isValidSampleIndex(this.width-1, this.height-1));
	assertTrue(james1.isValidSampleIndex(0, 0));


	}
	
	@Test
	public void TestlinearSampleIndex() {
		PanoramaParameters james1 = new PanoramaParameters(origin, 
				0, 0, this.horizontalvue, 1000, this.width, this.height);
		
		assertEquals(0, james1.linearSampleIndex(0, 0));
		assertEquals(2, james1.linearSampleIndex(2, 0));
		assertEquals(41, james1.linearSampleIndex(1, 1));
		assertEquals(799, james1.linearSampleIndex(39, 19));


	}
	
	
	
	
}
