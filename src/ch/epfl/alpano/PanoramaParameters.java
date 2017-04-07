package ch.epfl.alpano;


import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Preconditions.*;

/**
 * Represents the parameters for a panorama
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
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
	 * Create the parameters for a panorama
	 * @param observerPosition position of the observer
	 * @param observerElevation elevation of the observer
	 * @param centerAzimuth azimuth of the center of the field
	 * @param horizontalFieldOfView the total horizontal field of view
	 * @param maxDistance the maximum distance that can be seen
	 * @param width width of the field
	 * @param height height of the field
	 * @throws IllegalArgumentException if azimuth is not canonical, or if maxDistance, width or height are negative
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
	 * Gives the azimuth for a given x
	 * @param x the index
	 * @return the azimuth corresponding to the index
	 * @throws IllegalArgumentException if x is not in the field
	 * @see xForAzimuth
	 */
	public double azimuthForX(double x) {
		checkArgument(x >= 0 && x <= width() -1);
		
		double azimuth = floorMod(centerAzimuth() + (anglePerPixels() * (x - ((width() - 1) / 2.0))), PI2);		
		assert(Math.abs(angularDistance(azimuth, centerAzimuth())) <= horizontalFieldOfView/2 + 1e-10 && Azimuth.isCanonical(azimuth));
		
		return azimuth; 
	}
	
	/**
	 * Gives an x for an azimuth
	 * @param a the azimuth
	 * @return the index corresponding to the azimuth
	 * @throws IllegalArgumentException if the azimuth is not in the filed of view
	 */
	public double xForAzimuth(double a) { 	
		checkArgument(Math.abs(angularDistance(a, centerAzimuth())) <= horizontalFieldOfView/2 + 1e-10);
		
		double x = (angularDistance(centerAzimuth(), a) / anglePerPixels()) + ((width() -1) / 2.0);
		assert(x >= 0 && x <= width() - 1);
		
		return x;
	}
	
	/**
	 * Gives the altitude for a y
	 * @param y the vertical index
	 * @return the corresponding altitude
	 * @throws IllegalArgumentException if y is not in the field
	 * @see yForAltitude
	 */
	public double altitudeForY(double y) {
		checkArgument(y >= 0 && y <= height() - 1);
		
		double altitude = (((height() - 1) / 2.0) - y) * anglePerPixels();
		assert(Math.abs(altitude) <= verticalFieldOfView()/2);
		
		return altitude;
	}
	
	/**
	 * Gives a y for an altitude
	 * @param a the altitude 
	 * @return the corresponding index
	 * @throws IllegalArgumentException if the altitude is not in the field
	 * @see altitudeForY
	 */
	public double yForAltitude(double a) {
		checkArgument(Math.abs(a) <= verticalFieldOfView()/2);
		
		double y = ((height() - 1) / 2.0) - (a / anglePerPixels());
		assert(y >= 0 && y <= height() - 1);
		
		return y;
	}
	
	boolean isValidSampleIndex(int x, int y) {
		return  x >= 0 && x < width() && y >= 0 && y < height();
	}
	
	int linearSampleIndex(int x, int y) {
	    assert(isValidSampleIndex(x,y));
		return y * width() + x ;
	}
		
	/**
	 * Position of the observer
	 * @return the observer position
	 */
	public GeoPoint observerPosition() {
		return observerPosition;
	}
	
	/**
	 * Elevation of the observer
	 * @return the observer elevation in meters
	 */
	public int observerElevation() {
		return observerElevation;
	}
	
	/**
	 * Azimuth of the center
	 * @return the central azimuth
	 */
	public double centerAzimuth(){
		return centerAzimuth;
	}
	
	/**
	 * Total horizontal field of view
	 * @return the horizontal field of view
	 */
	public double horizontalFieldOfView() {
		return horizontalFieldOfView;
	
	}
	
	/**
	 * Maximum distance that can be seen by the observer
	 * @return the maximum distance from the observer
	 */
	public int maxDistance() {
		return maxDistance;
	}
	
	/**
	 * Width of the field
	 * @return the field width
	 */
	public int width() {
		return width;
	}
	
	/**
	 * Height of the field
	 * @return the field height
	 */
	public int height() {
		return height;
	}
	
	/**
	 * Angle per pixels
	 * @return rate of angle per pixels
	 */
	public double anglePerPixels() {
		return horizontalFieldOfView() / (width() - 1);
	}
	
	/**
	 * The total vertical field of view
	 * @return the vertical field of view
	 */
	public double verticalFieldOfView() {
		return anglePerPixels() * (height() - 1);
	}
	
}
