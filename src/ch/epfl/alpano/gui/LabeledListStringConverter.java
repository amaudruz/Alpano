package ch.epfl.alpano.gui;

import javafx.util.StringConverter;

/**
 * Represents a converter from a list of String to Integer and the other way around
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 * @see StringConverter<Integer>
 */
public final class LabeledListStringConverter extends StringConverter<Integer> {

    private final String[] strings;
    
    /**
     * Construct the converter with a list of string
     * 
     * @param strings the list of string
     */
    public LabeledListStringConverter(String...strings) {
        this.strings = strings;
    }
    
    private final static int DEFAULT_VALUE = -1;
    
    @Override
    public Integer fromString(String s) {
        if(s == null) {
            return DEFAULT_VALUE;
        }
        
        for(int i = 0; i < strings.length; i++) {
            if(strings[i].equals(s)) {
                return i;
            }
        }
        return DEFAULT_VALUE;
    }

    @Override
    public String toString(Integer i) {
        if(i == null) {
            return "";
        }
        return strings[i];
    }

}
