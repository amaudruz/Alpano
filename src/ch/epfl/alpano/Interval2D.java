package ch.epfl.alpano;
import static ch.epfl.alpano.Preconditions.*;
import java.util.Objects;
/**
 * Create a two dimensional interval given two one dimensional intervals (cartesian product)
 * Immutable class
 * @author Mathieu Chevalley (274698)
 *
 */
public final class Interval2D {

    private final Interval1D iX;
    private final Interval1D iY;
    
    /**
     * 
     * @param iX a 1d interval
     * @param iY a 1d interval
     * @throws NullPointerException if either of the intervals are null
     */
    public Interval2D(Interval1D iX, Interval1D iY){
        if(iX == null || iY == null){
            throw new NullPointerException();
        }
        this.iX = iX;
        this.iY = iY;
    }
    
    /**
     * 
     * @return the first interval
     */
    public Interval1D iX(){
        return iX;
    }
    
    /**
     * 
     * @return the second interval
     */
    public Interval1D iY(){
        return iY;
    }
    
    /**
     * 
     * @param x first element of the pair
     * @param y second element of the pair
     * @return true if this contains the pair
     */
    public boolean contains(int x, int y){
        return iX.contains(x) && iY.contains(y);
    }
    
    /**
     * 
     * @return the size of the interval
     */
    public int size(){
        return iX.size() * iY.size();
    }
    
    /**
     * 
     * @param that an other interval
     * @return the size of their intersection
     */
    public int sizeOfIntersectionWith(Interval2D that){
        return iX.sizeOfIntersectionWith(that.iX) * iY.sizeOfIntersectionWith(that.iY);
    }
    
    /**
     * 
     * @param that an other interval
     * @return the bounding union of both intervals
     */
    public Interval2D boundingUnion(Interval2D that){
        return new Interval2D(iX.boundingUnion(that.iX), iY.boundingUnion(that.iY));
    }
    
    /**
     * 
     * @param that an other interval
     * @return true if this and that are unionable
     */
    public boolean isUnionableWith(Interval2D that){
        return this.size() + that.size() - sizeOfIntersectionWith(that) == this.boundingUnion(that).size();
    }
    
    /**
     * 
     * @param that an other interval
     * @throws IllegalArgumentException if this and that are not unionable
     * @return the union of both intervals
     */
    public Interval2D union(Interval2D that){
        checkArgument(this.isUnionableWith(that));
        return this.boundingUnion(that);
    }
    
    @Override
    public boolean equals(Object thatO){
        if(!(thatO instanceof Interval2D)){
            return false;
        }
        if(thatO.getClass() == this.getClass()){
            return iX.equals(((Interval2D)thatO).iX) && iY.equals(((Interval2D)thatO).iY);
        }
        else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(iX.hashCode(), iY.hashCode());
    }
    
    @Override
    public String toString(){
        return iX.toString() + "x" + iY.toString();
    }
}
