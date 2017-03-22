package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

/**
 *An interface used for the used methods and constants of a DEM
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 * @see AutoClosable
 *
 */

public interface DiscreteElevationModel extends AutoCloseable {
	
	public static int SAMPLES_PER_DEGREE = 3600;
	
	public static double SAMPLES_PER_RADIAN = (SAMPLES_PER_DEGREE * 180/Math.PI) ;
	
	/**
	 * Returns the index corresponding to an angle
	 * @param angle
	 * @return index
	 */
	public static double sampleIndex(double angle) {
		
		
		return  angle * SAMPLES_PER_RADIAN;
	}
	
	/**
	 * @return the area of the DEM
	 * @see Interval2D
	 */
	abstract Interval2D extent();
	
	/**
	 * Method that will give the elevation of a location in the DEM
	 * 
	 * @param x : the first of the location
	 * @param y : the second coordinate of the location
	 * @return the elevation at the location
	 */
	abstract double elevationSample(int x, int y);
	
	/**
	 * Will make the union between the DEM and an other one
	 * 
	 * @param that the other DEM
	 * @return the union between the DEM this and an other DEM that
	 * @throws IllegalArgumentException from the builder of a union between two DEM
	 */
	default DiscreteElevationModel union(DiscreteElevationModel that)throws IllegalArgumentException {
		return new CompositeDiscreteElevationModel(this, that);
	
	}
		

}
