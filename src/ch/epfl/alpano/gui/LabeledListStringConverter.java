package ch.epfl.alpano.gui;

import javafx.util.StringConverter;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * Represents a converter from a list of String to Integer and the other way around
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 *@see StringConverter<Integer>
 */
public final class LabeledListStringConverter extends StringConverter<Integer> {

    private final String[] strings;
    
    /**
     * Construct teh converter with a list of string
     * 
     * @param strings the list of string
     */
    public LabeledListStringConverter(String...strings) {
        checkArgument(strings.length > 0);
        this.strings = strings;
    }
    
    @Override
    public Integer fromString(String arg0) {
        
        for(int i = 0; i < strings.length; i++) {
            if(strings[i].equals(arg0)) {
                return i;
            }
        }
        
        return 0;
    }

    @Override
    public String toString(Integer arg0) {
        checkArgument(arg0 >= 0 && arg0 < strings.length);
        return strings[arg0];
    }

}
