package ch.epfl.alpano.dem;


import static ch.epfl.alpano.Azimuth.*;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.*;
import static java.lang.Math.*;
import static ch.epfl.alpano.Math2.*;
import ch.epfl.alpano.Distance;

import ch.epfl.alpano.GeoPoint;


public final class ElevationProfile {
	private final ContinuousElevationModel elevationModel;
	private final GeoPoint[] positions;
	private final double length;

	
	public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length) {
		checkArgument(isCanonical(azimuth) && length >0);
		this.length = length;
		this.elevationModel = requireNonNull(elevationModel);
		requireNonNull(origin);
		
		this.positions = new GeoPoint[(int)((length/4096)) + 1];
		for (int i = 0 ; i < this.positions.length   ; ++i) {
			double latitude = asin((sin(origin.latitude()) * cos(Distance.toRadians(i* 4096))) 
					+  (cos(origin.latitude()) * sin(Distance.toRadians(i* 4096)) * cos(toMath(azimuth))));
		
			double longitude = floorMod(origin.longitude() - asin((sin(toMath(azimuth)) * sin(Distance.toRadians(i*4096)))/ cos(latitude)) + PI, 2*PI) -PI;
			positions[i] = new GeoPoint(longitude , latitude);
			
		}
	}
	
	public double slopeAt(double x) {
		checkArgument(x <= length && x >= 0);
		GeoPoint p =  this.positionAt(x);
		return this.elevationModel.slopeAt(p);
	}
	
	public double elevationAt(double x) {
		checkArgument(x <= length && x >= 0);
		GeoPoint p =  this.positionAt(x);
		return this.elevationModel.elevationAt(p);
	}
	
	
	public GeoPoint positionAt(double x) {
		checkArgument(x <= length && x >= 0);
		int x1 = (int)(x/4096.0);
		
		if (x1  >= this.positions.length - 1) {
			double longitude = lerp(this.positions[this.positions.length - 2].longitude(), 
					this.positions[this.positions.length - 1].longitude(),( x - (4096 *x1)) / 4096) ;
			double latitude = lerp(this.positions[this.positions.length - 2].latitude(), this.positions[this.positions.length - 1].latitude(), ( x - (4096 *x1)) / 4096);
			return new GeoPoint(longitude, latitude);
		}
		
		double longitude = lerp(this.positions[x1].longitude(), this.positions[x1 + 1].longitude(),( x - (4096 *x1)) / 4096) ;
		double latitude = lerp(this.positions[x1].latitude(), this.positions[x1 + 1].latitude(), ( x - (4096 *x1)) / 4096);
		return new GeoPoint(longitude, latitude);
		
	}
	
	
}
