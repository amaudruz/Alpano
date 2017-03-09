	 package ch.epfl.alpano.dem;


import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.*;
import static java.lang.Math.*;
import static ch.epfl.alpano.Math2.*;

import ch.epfl.alpano.GeoPoint;


public class ElevationProfile {
	private ContinuousElevationModel elevationModel;
	private GeoPoint[] positions;
	private GeoPoint origin;
	private double azimuth;
	private double length;

	
	ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length) {
		checkArgument(isCanonical(azimuth));
		this.azimuth = requireNonNull(azimuth);
		this.length = requireNonNull(length);
		this.origin = requireNonNull(origin);
		this.elevationModel = requireNonNull(elevationModel);
		
		this.positions = new GeoPoint[(int)(length/4096)];
		for (int i = 0 ; i < this.positions.length -1 ; ++i) {
			double longitude = asin((sin(origin.longitude())*cos(toRadians(length *i)))
					+ (cos(origin.longitude()) * sin(toRadians(length*i)) *cos(azimuth)));
			double latitude = floorMod((origin.latitude() - (asin((sin(azimuth) * sin(length*i)) / cos(longitude)) + PI)), 2*PI) -PI; 
			positions[i] = new GeoPoint(longitude , latitude);
		}
	}
	
	public double slopeAt(double x) {
		checkArgument(x > 0 && x <= length);
		GeoPoint p =  positionAt(x);
		return this.elevationModel.slopeAt(p);
	}
	
	public double elevationAt(double x) {
		checkArgument(x > 0 && x <= length);
		GeoPoint p =  positionAt(x);
		return this.elevationModel.elevationAt(p);
	}
	
	public GeoPoint positionAt(double x) {
		checkArgument(x > 0 && x <= length);
		int x1 = (int)(x/4096);
		double longitude = lerp(this.positions[x1].longitude(), this.positions[x1 + 1].longitude(), x / 4096 * (x1 + 1));
		double latitude = lerp(this.positions[x1].latitude(), this.positions[x1 + 1].latitude(), x / 4096 * (x1 + 1));
		return new GeoPoint(longitude, latitude);
	}

}
