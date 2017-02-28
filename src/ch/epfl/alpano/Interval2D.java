package ch.epfl.alpano;
import static ch.epfl.alpano.Preconditions.*;
import java.util.Objects;

public final class Interval2D {

    private final Interval1D iX;
    private final Interval1D iY;
    
    public Interval2D(Interval1D iX, Interval1D iY){
        if(iX == null || iY == null){
            throw new NullPointerException();
        }
        this.iX = iX;
        this.iY = iY;
    }
    
    public Interval1D iX(){
        return iX;
    }
    
    public Interval1D iY(){
        return iY;
    }
    
    public boolean contains(int x, int y){
        return iX.contains(x) && iY.contains(y);
    }
    
    public int size(){
        return iX.size() * iY.size();
    }
    
    //pas sur du tout de la manière de faire
    public int sizeOfIntersectionWith(Interval2D that){
        return iX.sizeOfIntersectionWith(that.iX) * iY.sizeOfIntersectionWith(that.iY);
    }
    
    public Interval2D boundingUnion(Interval2D that){
        return new Interval2D(iX.boundingUnion(that.iX), iY.boundingUnion(that.iY));
    }
    
    public boolean isUnionableWith(Interval2D that){
        return this.size() + that.size() - sizeOfIntersectionWith(that) == this.boundingUnion(that).size();
    }
    
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
