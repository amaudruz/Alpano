package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

/**
 * Class that represents a summit (immutable)
 * @author Mathieu Chevalley (274698)
 *
 */
public final class Summit {

    private final String name;
    private final GeoPoint position;
    private final int elevation;
    
    /**
     * public builder
     * @param name of the summit
     * @param position 
     * @param elevation (in metres)
     */
    public Summit(String name, GeoPoint position, int elevation){
        if(elevation < 0 || position == null || name.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.position = position;
        this.elevation = elevation;
    }
    
    /**
     * @return name of the summit
     */
    public String name(){
        return name;
    }
    
    /**
     * @return position of the summit
     */
    public GeoPoint position(){
        return position;
    }
    
    /**
     * @return the elevation of the summit
     */
    public int elevation(){
        return elevation;
    }
    
    @Override
    public String toString(){
        return name + " (" + position.longitude() + "," + position.latitude() + ") " + elevation;
    }
}
