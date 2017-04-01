package ch.epfl.alpano;
import static java.lang.Math.PI;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * Interface used for computations on azimuths
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 */


public interface Azimuth {

    /**
     * 
     * @param azimuth
     * @return <code>true</code> if the azimuth is canonical (between 0 and 2pi)
     */
    public static boolean isCanonical(double azimuth){
        
        return azimuth >= 0 && azimuth < PI2;
        
    }
    
    /**
     * Canonicalize an azimuth
     * 
     * @param azimuth
     * @return correct azimuth
     */
    public static double canonicalize(double azimuth){
        
        while(azimuth >= PI2){
            azimuth -= PI2;
        }
        while(azimuth < 0){
            azimuth += PI2;
        }
        
        return azimuth;
    }
    
    /**
     * Return an azimuth in the correct mathematical way
     * @param azimuth in geographical manner
     * @throws IllegalArgumentException if the azimuth is not canonical
     * @return the mathematical azimuth
     */
    public static double toMath(double azimuth){
        checkArgument(isCanonical(azimuth));
        
        return canonicalize(PI2 - azimuth);
    }
    
    /**
     * Return the azimuth in the geographical manner
     * @param angle mathematical angle
     * @return the correct azimuth
     * @throws IllegalArgumentException if the angle is not canonical
     */
    public static double fromMath(double angle){
        checkArgument(isCanonical(angle));
        
        return canonicalize(2 * PI - angle);
    }
    
    /**
     * Return a string that represents the octant in which the azimuth is
     * 
     * @param azimuth
     * @param n north string
     * @param e east string
     * @param s south string
     * @param w west string
     * @return a <code>String</code> position 
     * @throws IllegalArgumentException if azimuth is not canonical
     */
    public static String toOctantString(double azimuth, String n, String e, String s, String w){
        checkArgument(isCanonical(azimuth));
        
        double angle = canonicalize(- PI/8);
        int octant = 0;
        
        for(int i = 0; i < 8; ++i){
            
            if(azimuth >= angle && azimuth < angle + PI/4){
                octant = i;
            }
            
            angle = canonicalize(angle + PI/4);
        }
        //TODO pas d'autre façon?
        switch(octant){
        
            case 0:
                return n;
            case 1:
                return n + e;  
            case 2:
                return e;
            case 3:
                return s + e;
            case 4:
                return s;
            case 5:
                return s + w;
            case 6:
                return w;
            case 7:
                return n + w;
                
            default:
                return n;
        }
    }
    
    
}
