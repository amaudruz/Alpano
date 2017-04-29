package ch.epfl.alpano.gui;

import javafx.util.StringConverter;
import static ch.epfl.alpano.Preconditions.checkArgument;

public final class LabeledListStringConverter extends StringConverter<Integer> {

    private final String[] strings;
    
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
