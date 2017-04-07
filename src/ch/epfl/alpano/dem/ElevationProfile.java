package ch.epfl.alpano.dem;


import static ch.epfl.alpano.Azimuth.*;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.*;
import static java.lang.Math.*;
import static ch.epfl.alpano.Math2.*;
import ch.epfl.alpano.Distance;

import ch.epfl.alpano.GeoPoint;


/** 
 * Class used for a representation of the elevations and slopes at any point in a straight line
 * beginning from an original location to a wanted length
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 *
 */
public final class ElevationProfile {
    
	private final ContinuousElevationModel elevationModel;
	private final GeoPoint[] positions;
	private final double length;

	/**
	 * Construct a profile 
	 * @param elevationModel the elevation model which will give us the elevations and slopes
	 * @param origin the origin location from which begin the straight line
	 * @param azimuth the orientation of the straight line
	 * @param length the length of the straight line
	 * @throws IllegalArgumentException if the azimuth is not canonical or the length is non positive
	 * @throws NullPointerException if elevationModel or origin is <code>null</code>
	 */
	public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length) {
		checkArgument(isCanonical(azimuth) && length > 0);
		requireNonNull(origin);
		
		this.length = length;
		this.elevationModel = requireNonNull(elevationModel);
		
		//construct an array containing several positions split regularly  
		positions = new GeoPoint[(int)((length/4096)) + 2];
		
		for (int i = 0 ; i < positions.length; ++i) {
			double latitude = asin((sin(origin.latitude()) * cos(Distance.toRadians(i* 4096))) 
					+  (cos(origin.latitude()) * sin(Distance.toRadians(i* 4096)) * cos(toMath(azimuth))));
		
			
	        double longitude = angularDistance(asin((sin(toMath(azimuth)) * sin(Distance.toRadians(i*4096))) / cos(latitude)), origin.longitude());

			positions[i] = new GeoPoint(longitude , latitude);
		}
		
	}
	
	/** 
	 * Compute the slope at a certain distance from the original location 
	 * @param x : the distance from the original location
	 * @return the slope at a wanted distance from the original location
	 * @throws IllegalArgumentException if x is negative is bigger than the length
	 */
	
	public double slopeAt(double x) {
		checkArgument(x <= length && x >= 0);
		GeoPoint p =  positionAt(x);
		return elevationModel.slopeAt(p);
	}
	
	
	/**  
	 * Compute the elevation at a certain distance from the original location
	 * @param x : the distance from the original location
	 * @return the elevation at a wanted distance from the original location
	 * @throws IllegalArgumentException if x is negative is bigger than the length
	*/
	public double elevationAt(double x) {
		checkArgument(x <= length && x >= 0);
		GeoPoint p =  positionAt(x);
		return elevationModel.elevationAt(p);
	}
	
	
	
	/** 
	 * Compute the location at a certain distance from the origin location using bilinear interpolation
	 * @param x : the distance from the original location
	 * @return the location (GeoPoint) at a certain distance from the origin
	 * @throws IllegalArgumentException if x is negative is bigger than the length
	 */
	public GeoPoint positionAt(double x) {
		checkArgument(x <= length && x >= 0);
		int x1 = (int)(x/4096.0);

		double longitude = lerp(positions[x1].longitude(), positions[x1 + 1].longitude(), x / 4096 - x1) ;
		double latitude = lerp(positions[x1].latitude(), positions[x1 + 1].latitude(), x / 4096 - x1);
		return new GeoPoint(longitude, latitude);
	}
	
	
}
