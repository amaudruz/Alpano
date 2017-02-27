package ch.epfl.alpano;
import static ch.epfl.alpano.Preconditions.*;

/**
 * Create a on dimasional interval (immutable class)
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
}
