package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;
import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Preconditions.*;

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
     * @throws IllegalArgumentException if elevation is negative
     * @throws NullPointerException if name or position is null 
     */
    public Summit(String name, GeoPoint position, int elevation) {
        checkArgument(elevation >= 0);
        this.name = requireNonNull(name);
        this.position = requireNonNull(position);
        this.elevation = elevation;
    }
    
    /**
     * @return name of the summit
     */
    public String name() {
        return name;
    }
    
    /**
     * @return position of the summit (GeoPoint)
     * @see GeoPoint
     */
    public GeoPoint position() {
        return position;
    }
    
    /**
     * @return the elevation of the summit
     */
    public int elevation() {
        return elevation;
    }
    
    @Override
    public String toString() {
        return name() + " " + position() + " " + elevation();
    }
}
