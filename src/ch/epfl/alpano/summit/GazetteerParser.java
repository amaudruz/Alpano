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

public class GazetteerParser {

    private GazetteerParser(){}
    
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
    
    private static Summit readSummitLine(String line){
        String trimedLine = line.trim();
        String name = trimedLine.substring(26);
        
        String longitude = trimedLine.substring(0, 7);
        String[] hms = longitude.split(":");
        double longi = toRadians(Integer.parseInt(hms[0]), Integer.parseInt(hms[1]), Integer.parseInt(hms[2]));
        
        String latitude = trimedLine.substring(7,15);
        hms = latitude.split(":");
        double lati = toRadians(Integer.parseInt(hms[0]), Integer.parseInt(hms[1]), Integer.parseInt(hms[2]));
        
        GeoPoint position = new GeoPoint(longi, lati);
        
        int elevation = Integer.parseInt(trimedLine.substring(15, 19));
        
        return new Summit(name, position, elevation);
        
    }
    
    private static double toRadians(double degrees, double minutes, double seconds){
        double degree = degrees + minutes / 60 + seconds / 3600;
        return degree * Math.PI/180;
    }
}
