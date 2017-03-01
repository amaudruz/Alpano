
package ch.epfl.alpano;
import static ch.epfl.alpano.Preconditions.*;
import static java.lang.Math.*;

import java.util.Objects;

/**
 * Create a one dimasional interval (immutable class)
 * @author Mathieu Chevalley (274698)
 *
 */
public final class Interval1D {

    //Je sais pas s'il faut faire comme ça ou créer un tableau de int
    private final int includedFrom; 
    private final int includedTo;
    
    /**
     * Builder
     * @param includedFrom - beginning of the interval
     * @param includedTo - end of the interval
     * @throws IllegalArgumentException if includedTo is strictly smaller than includedFrom
     */
    public Interval1D(int includedFrom, int includedTo){
        checkArgument(includedFrom <= includedTo);
        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }
    
    /**
     * 
     * @return the beginning of the interval
     */
    public int includedFrom(){
        return includedFrom;
    }
    
    /**
     * 
     * @return end of the interval
     */
    public int includedTo(){
        return includedTo;
    }
    
    /**
     * Check if v is in the interval
     * @param v
     * @return true if v is inside
     */
    public boolean contains(int v){
        return v >= includedFrom && v <= includedTo;
    }
    
    /**
     * 
     * @return number of elements of the interval
     */
    public int size(){
        return includedTo - includedFrom + 1;
    }
    
    /**
     * 
     * @param that an other interval
     * @return the size of the intersection between this and that
     */
    public int sizeOfIntersectionWith(Interval1D that){
        if(this.contains(that.includedFrom)){
            if(this.contains(that.includedTo)){
                return that.size();
            }
            else{
                return includedTo - that.includedFrom + 1;
            }
        }
        else if(that.contains(includedFrom)){
            if(that.contains(includedTo)){
                return this.size();
            }
            else{
                return that.includedTo - includedFrom + 1;
            }
        }
        else{
            return 0;
        }
    }
    
    /**
     * 
     * @param that an other interval
     * @return the bounding union of this and that
     */
    public Interval1D boundingUnion(Interval1D that){
        return new Interval1D(min(includedFrom, that.includedFrom), max(includedTo, that.includedTo));
    }
    
    /**
     * 
     * @param that an other interval
     * @return true if this and that are unionable 
     */
    public boolean isUnionableWith(Interval1D that){
        return (this.boundingUnion(that)).size() == this.size() + that.size() - this.sizeOfIntersectionWith(that);
    }
    
    /**
     * 
     * @param that an other interval
     * @throws IllegalArgumentException if the two interval are not unionable
     * @return the union of both intervals
     */
    public Interval1D union(Interval1D that){
        checkArgument(this.isUnionableWith(that));
        return this.boundingUnion(that);
    }
    
    @Override
    public boolean equals(Object thatO){
        if(thatO instanceof Interval1D){
            if(thatO.getClass().equals(this.getClass())){
                return ( ((Interval1D)thatO).includedFrom == includedFrom && ((Interval1D)thatO).includedTo == includedTo);
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(includedFrom(), includedTo());
    }
    
    @Override
    public String toString(){
        return "["+includedFrom+".."+includedTo+"]";
    }
}

