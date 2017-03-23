package ch.epfl.alpano;


import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;
import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Preconditions.*;

public final class PanoramaParameters {
	
	private final  GeoPoint observerPosition;
	private final int observerElevation ;
	private final double centerAzimuth;
	private final double  horizontalFieldOfView;
	private final int maxDistance;
	private final int width;
	private final int height;
	
	public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth,double horizontalFieldOfView,int maxDistance, int width, int height) {
		checkArgument(Azimuth.isCanonical(centerAzimuth));
		checkArgument(maxDistance > 0 && width > 0 && height > 0);
		checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= PI2);
		checkArgument(observerElevation >= 0);
		
		this.observerPosition = requireNonNull(observerPosition);
		this.observerElevation = observerElevation;
		this.centerAzimuth = centerAzimuth;
		this.horizontalFieldOfView  = horizontalFieldOfView;
		this.maxDistance = maxDistance;
		this.width = width;
		this.height = height;	
		
		
	}
	
	public double azimuthForX(double x) {
		checkArgument(x >= 0 && x <= width() -1);
		
		double azimuth = floorMod(centerAzimuth() + (anglePerPixels()*(x -((width()-1)/2.0))), PI2);
		//azimuth = Azimuth.canonicalize(azimuth);
		assert(Math.abs(Math2.angularDistance(azimuth, centerAzimuth()) )<= horizontalFieldOfView/2 + 1e-10);
		
		return azimuth; 
	}
	
	
	public double xForAzimuth(double a) { 	
		checkArgument(Math.abs(Math2.angularDistance(a, centerAzimuth()) )<= horizontalFieldOfView/2 + 1e-10);
		
		double x = (angularDistance(centerAzimuth(),a ) / anglePerPixels()) +((width() -1) / 2.0);
		assert(x >= 0 && x <= width() - 1);
		
		return x;

	}
	
	public double altitudeForY(double y) {
		checkArgument(y >= 0 && y <= height() - 1);
		
		double altitude = (( (height()-1)/2.0) - y )* anglePerPixels();
		assert(Math.abs(altitude) <= verticalFieldOfView()/2);
		
		return altitude;
	}
	
	public double yForAltitude(double a) {
		checkArgument(Math.abs(a) <= verticalFieldOfView()/2);
		
		double y = ((height()-1)/2.0) + (-1)*(a /anglePerPixels());
		assert(y >= 0 && y < height());
		
		return y;
	}
	
	boolean isValidSampleIndex(int x, int y) {
		return ( x >= 0 && x < width() && y >= 0 && y < height() );
	}
	
	int linearSampleIndex(int x, int y) {
	    checkArgument(isValidSampleIndex(x,y));
		return (y * width()) + x ;
	}
				
	public GeoPoint observerPosition() {
		return this.observerPosition;
	}
	
	public int observerElevation() {
		return this.observerElevation;
	}
	public double centerAzimuth(){
		return this.centerAzimuth;
	}
	
	public double horizontalFieldOfView() {
		return this.horizontalFieldOfView;
	
	}
	
	public int maxDistance() {
		return this.maxDistance;
	}
	public int width() {
		return this.width;
	}
	
	public int height() {
		return this.height;
	}
	
	public double anglePerPixels() {
		return horizontalFieldOfView() / (width() - 1);
	}
	
	public double verticalFieldOfView() {
		return anglePerPixels() * (height() - 1);
	}
	
}
