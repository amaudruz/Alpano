package ch.epfl.alpano.summit;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        System.out.println(GazetteerParser.readSummitLine("  7:56:53 46:35:33  2472  H1 C02 D0 LAUBERHORN"));

    }
    
    
    @Test
    public void readFile() throws IOException{
        List<Summit> s = GazetteerParser.readSummitsFrom(new File("alps.txt"));
        for(Summit t : s){
            System.out.println(t);
        }
    }
    
    @Test
    public void toRadianTest(){
        assertEquals(0.017453292519943, GazetteerParser.toRadians(1, 0, 0),1e-10);
        assertEquals(0.00029088820866572, GazetteerParser.toRadians(0, 1, 0),1e-10);
        assertEquals(4.8481368110954E-6 , GazetteerParser.toRadians(0, 0, 1),1e-10);

    }
    

}
