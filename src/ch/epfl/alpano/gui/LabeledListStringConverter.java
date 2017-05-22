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
     * Construct the converter with a list of string
     * 
     * @param strings the list of string
     */
    public LabeledListStringConverter(String...strings) {
        //TODO
        checkArgument(strings.length > 0);
        this.strings = strings;
    }
    
    @Override
    public Integer fromString(String s) {
        if(s == null) {
            return 0;
        }
        
        for(int i = 0; i < strings.length; i++) {
            if(strings[i].equals(s)) {
                return i;
            }
        }
        //TODO
        return 0;
    }

    @Override
    public String toString(Integer i) {
        if(i == null) {
            return "";
        }
        //TODO
        checkArgument(i >= 0 && i < strings.length);
        return strings[i];
    }

}
