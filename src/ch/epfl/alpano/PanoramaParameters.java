package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;

public final class PanoramaParameters {
	
	private final  GeoPoint observerPosition;
	private final int observerElevation ;
	private final double centerAzimuth;
	private final double  horizontalFieldOfView;
	private final int maxDistance;
	private final int width;
	private final int height;
	
	public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth,double horizontalFieldOfView,int maxDistance, int width, int height) {
		Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
		Preconditions.checkArgument(maxDistance > 0 & width > 0 & height > 0);
		Preconditions.checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= 2*PI);
		this.observerPosition = requireNonNull(observerPosition);
		this.observerElevation = observerElevation;
		this.centerAzimuth = centerAzimuth;
		this.horizontalFieldOfView  = horizontalFieldOfView;
		this.maxDistance = maxDistance;
		this.width = width;
		this.height = height;
		
		
	}
	
	public double azimuthForX(double x) {
		Preconditions.checkArgument(x > 0 && x <= this.width -1);
		return this.centerAzimuth() + (anglePerPixels()*(x -(width/2))); 
	}
	
	
	public double xForAzimuth(double a) { 	
		Preconditions.checkArgument(a > this.centerAzimuth-(this.horizontalFieldOfView/2) && a < this.centerAzimuth+(this.horizontalFieldOfView/2));  	
		return (width/2) + ((a -this.centerAzimuth)/  anglePerPixels());
	}
	
	public double altitudeForY(double y) {
		Preconditions.checkArgument(y >= 0 && y < this.height);
		return y * anglePerPixels();
	}
	
	public double yForAltitude(double a) {
		Preconditions.checkArgument(a >= 0 && a <= this.verticalFieldOfView() );
		return (a / this.anglePerPixels());
	}
	
	boolean isValidSampleIndex(int x, int y) {
		return ( x >= 0 && x < this.width && y >= 0 && y < this.height );
		}
	
	int linearSampleIndex(int x, int y) {
		return (y*this.width) + x ;
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
