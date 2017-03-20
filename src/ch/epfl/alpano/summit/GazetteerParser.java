package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import static ch.epfl.alpano.Preconditions.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.alpano.GeoPoint;

/**
 * Class used to read a file containing summits (cannot be instanciate)
 * @author Mathieu Chevalley (274698)
 *
 */
public class GazetteerParser {

    private GazetteerParser(){}
    
    /**
     * read the summits from a file
     * @param file
     * @return a list of summit
     * @throws IOException
     */
    public static List<Summit> readSummitsFrom(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII));
        List<Summit> summits = new ArrayList<Summit>();
        String line = "";
        while((line = reader.readLine()) != null){
            summits.add(readSummitLine(line));
        }
        
        reader.close();
        return Collections.unmodifiableList(summits);
    }
    
    public static Summit readSummitLine(String line) throws IOException {
        checkArgument(!line.isEmpty() && line.length() > 36);
        try {

            String longitude = line.substring(0, 9).trim();
            String[] hms = longitude.split(":");
            double longi = toRadians(Integer.parseInt(hms[0]), Integer.parseInt(hms[1]), Integer.parseInt(hms[2]));

            String latitude = line.substring(10,18).trim();
            hms = latitude.split(":");
            double lati = toRadians(Integer.parseInt(hms[0]), Integer.parseInt(hms[1]), Integer.parseInt(hms[2]));

            GeoPoint position = new GeoPoint(longi, lati);
            

            int elevation = Integer.parseInt(line.substring(20, 24).trim());
            
            String name = line.substring(36).trim();
            
            
            return new Summit(name, position, elevation);
        }
        catch(Exception e) {
            throw new IOException();
        }
        
    }
    
    public static double toRadians(double degrees, double minutes, double seconds) {
        checkArgument(degrees >= 0 && minutes >= 0 && seconds >= 0);
        double degree = degrees + minutes / 60 + seconds / 3600;
        return degree * Math.PI/180;
    }
    
}
