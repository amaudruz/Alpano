package ch.epfl.alpano.gui;
import static ch.epfl.alpano.Panorama.*;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama;
@FunctionalInterface
public interface ChannelPainter {
	
	float valueAt(int x, int y);
	
	static ChannelPainter maxDistanceToNeighbors(Panorama p) {
		 
		 return (x, y) -> Math.max(Math.max(p.distanceAt(x+1, y, 0), p.distanceAt(x, y+1, 0)),
				 Math.max(p.distanceAt(x-1, y, 0), p.distanceAt(x, y-1, 0))) - p.distanceAt(x, y); 
	}
	
	default ChannelPainter add(float d) {
		return (x,y) -> valueAt(x, y) + d;
	}
	
	default ChannelPainter mul(float d) {
		return (x,y) -> valueAt(x, y) * d;
	}
	
	default ChannelPainter sub(float d) {
		return (x,y) ->valueAt(x, y) - d;
	}
	default ChannelPainter div(float d) {
		return (x,y) -> valueAt(x, y) / d;
	}
	
	default ChannelPainter map(DoubleUnaryOperator p)  {
		return (x,y) -> (float)p.applyAsDouble((valueAt(x,y)));
	}
	
	default ChannelPainter clamp() {
		return (x,y) -> Math.max(0, Math.min(valueAt(x,y), 1) );
	}
	
	default ChannelPainter cycle() {
		return (x,y) -> valueAt(x,y) - (int)valueAt(x,y);
	}
	default ChannelPainter invert() {
		return (x,y) -> 1-valueAt(x,y);
	}
	
}
