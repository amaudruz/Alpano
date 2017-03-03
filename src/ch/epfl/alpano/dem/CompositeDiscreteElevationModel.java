package ch.epfl.alpano;
import java.util.Objects;
import static java.util.Objects.requireNonNull;

final class CompositeDiscreteElevationModel implements DiscreteElevationModel{
	
	private Interval2D MNT;
	private DiscreteElevationModel dem1;
	private DiscreteElevationModel dem2;

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
		// TODO Auto-generated method stub
			if (this.MNT.iX().contains(x) && this.MNT.iY().contains(y)) {
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
