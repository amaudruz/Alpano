package ch.epfl.alpano;

public interface DiscreteElevationModel extends AutoCloseable{
	
	public static int SAMPLES_PER_DEGREE = 3600;
	
	public static double SAMPLES_PER_RADIAN = (3600 * Math.PI/180) ;
	
	
	public static double sampleIndex(double angle) {
		
		
		return  angle * SAMPLES_PER_RADIAN;
	}
	
	
	abstract Interval2D extent();
	
	abstract double elevationSample(int x, int y);
	
	default DiscreteElevationModel union(DiscreteElevationModel that)throws IllegalArgumentException {
		Interval2D union = this.extent().union(that.extent());
		return new CompositeDiscreteElevationModel(this, that);
	}
	
	

}
