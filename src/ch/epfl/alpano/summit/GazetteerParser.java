package ch.epfl.alpano.summit;

import java.io.BufferedReader;
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
    public static List<Summit> readSummitsFrom(File file) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII));
        List<Summit> summits = new ArrayList<Summit>();
        String line = "";
        while((line = reader.readLine()) != null){
            summits.add(readSummitLine(line));
        }
        
        reader.close();
        return Collections.unmodifiableList(summits);
    }
    
    public static Summit readSummitLine(String line){
        if(line.isEmpty() || line.length() < 34){
            throw new IllegalArgumentException();
        }
        
        try{
            
            String trimedLine = line.trim();
            String name = trimedLine.substring(26);
            System.out.println(trimedLine);
            System.out.println(name);
            for(int i = 0; i < trimedLine.length(); ++i){
                System.out.println((char) trimedLine.charAt(i) + " " + i);
            }
            
            String longitude = trimedLine.substring(0, 7);
            String[] hms = longitude.split(":");
            double longi = toRadians(Integer.parseInt(hms[0]), Integer.parseInt(hms[1]), Integer.parseInt(hms[2]));
            System.out.println(longitude);
            String latitude = trimedLine.substring(7,15);
            hms = latitude.split(":");
            double lati = toRadians(Integer.parseInt(hms[0]), Integer.parseInt(hms[1]), Integer.parseInt(hms[2]));
            System.out.println(latitude);
            GeoPoint position = new GeoPoint(longi, lati);
            
            int elevation = Integer.parseInt(trimedLine.substring(15, 19));
            System.out.println(elevation);
            return new Summit(name, position, elevation);
        }
        catch(Exception e){
            throw new IllegalArgumentException("line wrongly formated");
        }
        
    }
    
    private static double toRadians(double degrees, double minutes, double seconds){
        double degree = degrees + minutes / 60 + seconds / 3600;
        return degree * Math.PI/180;
    }
}
