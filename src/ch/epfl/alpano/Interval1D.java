
package ch.epfl.alpano;
import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Preconditions.*;
import static java.lang.Math.*;

import java.util.Objects;

/**
 * Create a one dimasional interval (immutable class)
 * @author Mathieu Chevalley (274698)
 *  
 */
public final class Interval1D {

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
     * @return <code>true</code> if v is inside, <code>false</code> otherwise
     */
    public boolean contains(int v){
        return v >= includedFrom() && v <= includedTo();
    }
    
    /**
     * 
     * @return number of elements of the interval
     */
    public int size(){
        return includedTo() - includedFrom() + 1;
    }
    
    /**
     * 
     * @param that an other interval
     * @throws NullPointerException if that is null
     * @return the size of the intersection between this and that
     */
    public int sizeOfIntersectionWith(Interval1D that){
        requireNonNull(that);
        //TODO est-ce que c'est la méthode la plus rapide?
        if(this.contains(that.includedFrom())){
            if(this.contains(that.includedTo())){
                return that.size();
            }
            else{
                return includedTo() - that.includedFrom() + 1;
            }
        }
        else if(that.contains(includedFrom())){
            if(that.contains(includedTo())){
                return this.size();
            }
            else{
                return that.includedTo() - includedFrom() + 1;
            }
        }
        else{
            return 0;
        }
    }
    
    /**
     * 
     * @param that an other interval
     * @throws NullPointerException if that is null
     * @return the bounding union of this and that
     */
    public Interval1D boundingUnion(Interval1D that){
        requireNonNull(that);
        return new Interval1D(min(includedFrom(), that.includedFrom()), max(includedTo(), that.includedTo()));
    }
    
    /**
     * 
     * @param that an other interval
     * @throws NullPointerException if that is null
     * @return true if this and that are unionable 
     */
    public boolean isUnionableWith(Interval1D that){
        requireNonNull(that);
        return (boundingUnion(that)).size() == size() + that.size() - sizeOfIntersectionWith(that);
    }
    
    /**
     * 
     * @param that an other interval
     * @return the union of both intervals
     * @throws IllegalArgumentException if the two interval are not unionable
     * @throws NullPointerException if that is null
     */
    public Interval1D union(Interval1D that){
        requireNonNull(that);
        checkArgument(isUnionableWith(that));
        
        return boundingUnion(that);
    }
    
    @Override
    public boolean equals(Object thatO){
        requireNonNull(thatO);
        //TODO c'est la bonne méthode?
        if(thatO instanceof Interval1D){
            if(thatO.getClass().equals(this.getClass())){
                return (((Interval1D)thatO).includedFrom() == includedFrom() && ((Interval1D)thatO).includedTo() == includedTo());
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
        return "["+includedFrom()+".."+includedTo()+"]";
    }
}

