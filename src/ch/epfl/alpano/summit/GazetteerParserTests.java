package ch.epfl.alpano.summit;

import org.junit.Test;

public class GazetteerParserTests {

    @Test(expected = IllegalArgumentException.class)
    public void readLineEmpty(){
        GazetteerParser.readSummitLine("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void readLineTrivial(){
        GazetteerParser.readSummitLine("20134 summit");
    }
    
    @Test
    public void readLineOnNonTrivial(){
        System.out.println(GazetteerParser.readSummitLine("7:56:53 46:35:33  2472  H1 C02 D0 LAUBERHORN"));

    }
}
