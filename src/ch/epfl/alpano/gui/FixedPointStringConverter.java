package ch.epfl.alpano.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import static ch.epfl.alpano.Preconditions.checkArgument;

import javafx.util.StringConverter;
/**
 * Represents a converter from a String to an Integer and the other way around... ?
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 *@see StringConverter<Integer>
 */
public final class FixedPointStringConverter extends StringConverter<Integer> {

    private final int decimal;
    
    /**
     * Create a converter given the number of decimal to represent
     * @param decimal
     */
    public FixedPointStringConverter(int decimal) {
        this.decimal = decimal;
    }
    
    @Override
    public Integer fromString(String arg0) { 
        if(arg0 == null) {
            return 0;
        }
        BigDecimal d = new BigDecimal(arg0);    
        return d.movePointRight(decimal).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    @Override
    public String toString(Integer arg0) {
        if(arg0 == null) {
            return "";
        }
        BigDecimal d = new BigDecimal(arg0);
        return d.movePointLeft(decimal).stripTrailingZeros().toPlainString();
    }

}
