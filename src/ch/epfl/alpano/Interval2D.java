package ch.epfl.alpano;

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
}
