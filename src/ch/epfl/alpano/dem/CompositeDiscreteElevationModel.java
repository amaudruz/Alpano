package ch.epfl.alpano.dem;
import java.util.Objects;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Interval2D;


/**Class thet represents a union of two DEM, implements discrete elevation model.
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 *
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel{
	
	private final Interval2D MNT;
	private final DiscreteElevationModel dem1;
	private final DiscreteElevationModel dem2;

	/**Construct a dem with the union of the intervals and their elevations
	 * 
	 * @param dem1 : first dem
	 * @param dem2 :second dem
	 */
	CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2) {
		
		this.MNT = 	requireNonNull(dem1.extent()).union(dem2.extent());
		this.dem1 = requireNonNull(dem1);
		this.dem2 = requireNonNull(dem2);
		

		
		
	}
	@Override
	public Interval2D extent() {
		return this.MNT;
		
	}

	@Override
	public double elevationSample(int x, int y) {

			if (this.MNT.contains(x,y)) {
				if (this.dem1.extent().contains(x, y)) {
					return dem1.elevationSample(x, y);
				}
				else {
					return dem2.elevationSample(x, y);
				}
				
			}
			
			else {
				throw new IllegalArgumentException("the point does not belong to the MNT");
			}
			
	}

	@Override
	public void close() throws Exception {
		
		dem1.close();
		dem2.close();	
	}

}
