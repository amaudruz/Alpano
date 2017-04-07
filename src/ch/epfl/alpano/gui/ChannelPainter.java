package ch.epfl.alpano.gui;
import static ch.epfl.alpano.Panorama.*;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama;
/**
 * Functional representing a channel painter
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 *  *
 */
@FunctionalInterface
public interface ChannelPainter {
	
	float valueAt(int x, int y);
	
	/**Compute the Channel painter wich will return a value with respect to some inforamtion about the panorama
	 * 
	 * @param p the panorama
	 * @return The channel painter wanted
	 */
	static ChannelPainter maxDistanceToNeighbors(Panorama p) {
		 
		 return (x, y) -> Math.max(Math.max(p.distanceAt(x+1, y, 0), p.distanceAt(x, y+1, 0)),
				 Math.max(p.distanceAt(x-1, y, 0), p.distanceAt(x, y-1, 0))); 
	}
	
	/**Add an amount the value returned by the channel painter
	 * 
	 * @param d the amount
	 * @return the channel painter with the addition
	 */
	default ChannelPainter add(float d) {
		return (x,y) -> valueAt(x, y) + d;
	}
	
	/**Multiply by an amount the value returned by the channel painter
	 * 
	 * @param d the amount
	 * @return the channel painter with the multiplication
	 */
	default ChannelPainter mul(float d) {
		return (x,y) -> valueAt(x, y) * d;
	}
	
	/**Substract by an amount the value returned by the channel painter
	 * 
	 * @param d the amount
	 * @return the channel painter with the substraction
	 */
	default ChannelPainter sub(float d) {
		return (x,y) ->valueAt(x, y) - d;
	}
	
	/**Divide by an amount the value returned by the channel painter
	 * 
	 * @param d the amount
	 * @return the channel painter with the division
	 */
	default ChannelPainter div(float d) {
		return (x,y) -> valueAt(x, y) / d;
	}
	
	/**Apply the double urnary operator the the value of the channel painter
	 * 
	 * @param p the double urnary operator
	 * @return the channel painter to which the function was applied
	 */
	default ChannelPainter map(DoubleUnaryOperator p)  {
		return (x,y) -> (float)p.applyAsDouble((valueAt(x,y)));
	}
	
	/**Apply a certain function (clamp) to the channel painter
	 * 
	 * @return the channel painter to which the function (clamp) was applied
	 */
	default ChannelPainter clamp() {
		return (x,y) -> Math.max(0, Math.min(valueAt(x,y), 1) );
	}
	
	/**Apply a certain function (cycle) to the channel painter
	 * 
	 * @return the channel painter to which the function (cycle) was applied
	 */
	default ChannelPainter cycle() {
		return (x,y) -> valueAt(x,y) - (int)valueAt(x,y);
	}
	
	/**Apply a certain function (invert) to the channel painter
	 * 
	 * @return the channel painter to which the function (invert) was applied
	 */
	default ChannelPainter invert() {
		return (x,y) -> 1-valueAt(x,y);
	}
	
}
