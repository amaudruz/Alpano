package ch.epfl.alpano;

/**
 * Interface to check preconditions
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 */
public interface Preconditions {
    
    /**
     * 
     * @param b a condition
     * @throws IllegalArgumentException if b is false
     */
    static void checkArgument(boolean b){
        if(!b){
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * 
     * @param b a condition
     * @param message a message
     * @throws IllegalArgumentException if b is false, with the message
     */
    static void checkArgument(boolean b, String message){
        if(!b){
            throw new IllegalArgumentException(message);
        }
    }

}
