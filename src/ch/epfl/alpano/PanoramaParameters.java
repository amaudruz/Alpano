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
		checkArgument(x > 0 && x <= width() -1);
		return floorMod(centerAzimuth() + (anglePerPixels()*(x -(width()/2))), PI2); 
	}
	
	
	public double xForAzimuth(double a) { 	
		if (centerAzimuth() - (horizontalFieldOfView()/2) < 0) {
			checkArgument(( a >= floorMod(centerAzimuth() - (horizontalFieldOfView()/2), PI2) && a <= (PI2)) ||
			(a >= 0 && a <= centerAzimuth() + (horizontalFieldOfView()/2)));
		}
		
		if (centerAzimuth() + (horizontalFieldOfView()/2) > PI2) {
			checkArgument(( a >= centerAzimuth() - (horizontalFieldOfView()/2)  && a <= 0) ||
			(a >= 0 && a <= floorMod(centerAzimuth() + (horizontalFieldOfView()/2), PI2)));
		}
		
		return (angularDistance(a, centerAzimuth()) / anglePerPixels()) +((width() - 1) / 2);

	}
	
	public double altitudeForY(double y) {
		checkArgument(y >= 0 && y < height());
		
		double altitude = (y - height()/2) * anglePerPixels();
		assert(altitude >= 0 && altitude <= verticalFieldOfView());
		
		return altitude;
	}
	
	public double yForAltitude(double a) {
		checkArgument(a >= 0 && a <= verticalFieldOfView());
		
		double y = (height()/2) + (a /anglePerPixels());
		assert(y >= 0 && y < height());
		
		return y;
	}
	
	boolean isValidSampleIndex(int x, int y) {
		return ( x >= 0 && x < width() && y >= 0 && y < height() );
	}
	
	int linearSampleIndex(int x, int y) {
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
		return this.height();
	}
	
	public double anglePerPixels() {
		return this.horizontalFieldOfView/ (this.width-1);
	}
	
	public double verticalFieldOfView() {
		return anglePerPixels()*(this.height-1);
	}
	
}
