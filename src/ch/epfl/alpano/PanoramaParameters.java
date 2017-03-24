package ch.epfl.alpano;


import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;
import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Preconditions.*;

/**
 * Represents the parameters for a panorama
 * @author Mathieu Chevalley (274698)
 *
 */
public final class PanoramaParameters {
	
	private final  GeoPoint observerPosition;
	private final int observerElevation ;
	private final double centerAzimuth;
	private final double  horizontalFieldOfView;
	private final int maxDistance;
	private final int width;
	private final int height;
	
	/**
	 * Public constructor
	 * @param observerPosition
	 * @param observerElevation
	 * @param centerAzimuth
	 * @param horizontalFieldOfView
	 * @param maxDistance
	 * @param width
	 * @param height
	 * @throws IllegalArgumentExcpetion if azimuth is not canonical, or if maxDistance, width or height are negative
	 * or if horizontalFieldOfView is less than 0 or greater that 2*pi
	 * @throws NullPointerException if observerPosition is null
	 */
	public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth,double horizontalFieldOfView,int maxDistance, int width, int height) {
		checkArgument(Azimuth.isCanonical(centerAzimuth));
		checkArgument(maxDistance > 0 && width > 0 && height > 0);
		checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= PI2);
		
		this.observerPosition = requireNonNull(observerPosition);
		this.observerElevation = observerElevation;
		this.centerAzimuth = centerAzimuth;
		this.horizontalFieldOfView  = horizontalFieldOfView;
		this.maxDistance = maxDistance;
		this.width = width;
		this.height = height;	
		
		
	}
	
	/**
	 * 
	 * @param x the index
	 * @return the azimuth corresponding to the index
	 * @throws IllegalArgumentException if x is not in the field
	 * @see xForAzimuth
	 */
	public double azimuthForX(double x) {
		checkArgument(x >= 0 && x <= width() -1);
		
		double azimuth = floorMod(centerAzimuth() + (anglePerPixels()*(x -((width()-1)/2.0))), PI2);
		azimuth = Azimuth.canonicalize(azimuth);
		assert(Math.abs(Math2.angularDistance(azimuth, centerAzimuth()) )<= horizontalFieldOfView/2 + 1e-10 && Azimuth.isCanonical(azimuth));
		
		return azimuth; 
	}
	
	/**
	 * 
	 * @param a the azimuth
	 * @return the index corresponding to the azimuth
	 * @throws IllegalArgumentException if the azimuth is not in the filed of view
	 */
	public double xForAzimuth(double a) { 	
		checkArgument(Math.abs(Math2.angularDistance(a, centerAzimuth()) )<= horizontalFieldOfView/2 + 1e-10);
		
		double x = (angularDistance(centerAzimuth(),a ) / anglePerPixels()) +((width() -1) / 2.0);
		assert(x >= 0 && x <= width() - 1);
		
		return x;

	}
	
	/**
	 * 
	 * @param y the vertical index
	 * @return the corresponding altitude
	 * @throws IllegalArgumentException if y is not in the field
	 * @see yForAltitude
	 */
	public double altitudeForY(double y) {
		checkArgument(y >= 0 && y <= height() - 1);
		
		double altitude = (( (height()-1)/2.0) - y )* anglePerPixels();
		assert(Math.abs(altitude) <= verticalFieldOfView()/2);
		
		return altitude;
	}
	
	/**
	 * 
	 * @param a the altitude 
	 * @return the corresponding index
	 * @throws IllegalArgumentException if the altitude is not in the field
	 * @see altitudeForY
	 */
	public double yForAltitude(double a) {
		checkArgument(Math.abs(a) <= verticalFieldOfView()/2);
		
		double y = ((height()-1)/2.0) - (a /anglePerPixels());
		assert(y >= 0 && y <= height() - 1);
		
		return y;
	}
	
	boolean isValidSampleIndex(int x, int y) {
		return ( x >= 0 && x < width() && y >= 0 && y < height() );
	}
	
	int linearSampleIndex(int x, int y) {
	    assert(isValidSampleIndex(x,y));
		return (y * width()) + x ;
	}
		
	/**
	 * 
	 * @return the observer position
	 */
	public GeoPoint observerPosition() {
		return this.observerPosition;
	}
	
	/**
	 * 
	 * @return the observer elevation in meters
	 */
	public int observerElevation() {
		return this.observerElevation;
	}
	
	/**
	 * 
	 * @return the central azimuth
	 */
	public double centerAzimuth(){
		return this.centerAzimuth;
	}
	
	/**
	 * 
	 * @return the horizontal fiel of view
	 */
	public double horizontalFieldOfView() {
		return this.horizontalFieldOfView;
	
	}
	
	/**
	 * 
	 * @return the maximum distance from the observer
	 */
	public int maxDistance() {
		return this.maxDistance;
	}
	
	/**
	 * 
	 * @return the field width
	 */
	public int width() {
		return this.width;
	}
	
	/**
	 * 
	 * @return the field height
	 */
	public int height() {
		return this.height;
	}
	
	/**
	 * 
	 * @return rate of angle per pixels
	 */
	public double anglePerPixels() {
		return horizontalFieldOfView() / (width() - 1);
	}
	
	/**
	 * 
	 * @return the vertical field of view
	 */
	public double verticalFieldOfView() {
		return anglePerPixels() * (height() - 1);
	}
	
}
