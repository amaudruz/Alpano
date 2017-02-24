package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;
import static ch.epfl.alpano.Preconditions.checkArgument;;

/**
 * A complement to java.lang.Math
 *  
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 */
public interface Math2 {
    
    double PI2 = 2 * Math.PI;
    
    /**
     * Compute the square of a number
     * @param x the number
     * @return the squared number
     */
    public static double sq(double x){
        return x * x;
    }
    
    /**
     * Return the default modulo
     * @param x the numerator
     * @param y the divisor
     * @return the modulo
     */
    public static double floorMod(double x, double y){
        double mod = x - y * Math.floor(x/y);
        return mod;
    }

    /**
     * Compute the haversin of a number
     * @param x the number
     * @return the corresponding haversin
     */
    public static double haversin(double x){
        double sinus = Math.sin(x/2);
        return sinus * sinus;
    }
    
    /**
     * The difference between two angles
     * @param a1
     * @param a2
     * @return the difference
     */
    public static double angularDistance(double a1, double a2){
        return floorMod(a2 - a1 + Math.PI, PI2) - Math.PI;
    }
    
    /**
     * Linear interpolation of two points
     * @param y0 first point
     * @param y1 second point
     * @param x the abscissa
     * @return the ordinate of x, using linear interpolation
     */
    public static double lerp(double y0, double y1, double x){
        return (y1 - y0) * x + y0;
    }
    
    /**
     * Bilinear interpolation of 4 points
     * @param z00
     * @param z10
     * @param z01
     * @param z11
     * @param x the abscissa
     * @param y the ordinate
     * @return the result using a bilinear interpolation
     */
    public static double bilerp(double z00, double z10, double z01, double z11,
            double x, double y){
        
        double lerp1 = lerp(z00, z10, x);
        double lerp2 = lerp(z01, z11, x);
        return lerp(lerp1, lerp2, y);
        
    }
    
    /**
     * Find an interval containing a root of a given function
     * @param f a function
     * @param minX the lower bound
     * @param maxX the upper bound
     * @param dX the size of the interval
     * @return the lower bound of the interval, infinity if no interval has been found
     */
    public static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX){
        
        for (double i = minX; i <= maxX; i = i+dX) {
            
            if (f.applyAsDouble(i) * f.applyAsDouble(i+dX) <= 0){
                return i;            
            }    
            
         } 
        
        return Double.POSITIVE_INFINITY;
    }
    
    /**
     * Find an interval containing the root of a function using binary search
     * @param f the function
     * @param x1 lower bound
     * @param x2 upper bound
     * @param epsilon a size giving the precision
     * @throws IllegalArgumentException if the given interval do not contain a root
     * @throws IllegalArgumentException if epsilon is not positive
     * @return the lower bound of the interval
     */
    public static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon){
        checkArgument(f.applyAsDouble(x1) * f.applyAsDouble(x2) < 0);
           
        
        checkArgument(epsilon > 0);
        
        double xm = (x1 + x2)/2;
        
        
        if(Math.abs(x1 - x2) < epsilon){
            return x1;
        }
        
        if(f.applyAsDouble(xm) * f.applyAsDouble(x1) < 0){
            return improveRoot(f, x1, xm, epsilon);
        }
        else if(f.applyAsDouble(xm) * f.applyAsDouble(x2) < 0){
            return improveRoot(f, xm, x2, epsilon);
        }
        
        return xm;
    }
}
