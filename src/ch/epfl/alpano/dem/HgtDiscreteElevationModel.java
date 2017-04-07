package ch.epfl.alpano.dem;

import java.io.File;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Integer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import static ch.epfl.alpano.Azimuth.*;
import static ch.epfl.alpano.Preconditions.*;




/**
 * A class that represents a discrete elevation model taken from a certain file
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 * @see DiscreteElevationModel
 */


public final class HgtDiscreteElevationModel implements DiscreteElevationModel {

    private ShortBuffer buffer;
    private final Interval2D extent;
    private int fromLatitude;
    private int fromLongitude;
    

    /**
     * Construct a dem from a hgt file
     * @param file the hgt file
     * @throws IllegalArgumentException if the file name is not properly
     * formated, or if an error occurs when reading the file
     */
    public HgtDiscreteElevationModel(File file) {
        String fileName = file.getName();
        
        checkArgument(fileName.length() == 11, "wrong length");
        checkArgument(fileName.charAt(0) == 'N' || fileName.charAt(0) == 'S', "Should begin by N or S");
        checkArgument(fileName.charAt(3) == 'E' || fileName.charAt(3) == 'W', "Should be E or W");

        try {
            fromLatitude = Integer.parseInt(fileName.substring(1, 3));
            if(fileName.charAt(0) != 'N') {
                fromLatitude = (int) (-fromLatitude);
            }
            fromLongitude = Integer.parseInt(fileName.substring(4, 7));
            if(fileName.charAt(3) == 'W') {
                fromLongitude = (int) (-fromLongitude);
            }

        }
        catch(NumberFormatException e) {
            //if NumberFormatException, the file name is wrongly formatted
            throw new IllegalArgumentException();
        }
        
        checkArgument(fileName.substring(7).equals(".hgt"), "should be a .hgt");
        
        //create the extent
        extent = new Interval2D(new Interval1D(fromLongitude * 3600, (fromLongitude + 1) * 3600), new Interval1D(fromLatitude * 3600, (fromLatitude + 1) * 3600));
       
        try(FileInputStream fileStream = new FileInputStream(file)) {
            
            long length = file.length();
            
            checkArgument(length == 25934402, "wrong length");
            
            buffer = fileStream.getChannel().map(MapMode.READ_ONLY, 0, length).asShortBuffer();
        }
        catch(IOException e) {
            //if exception, input file wrongly formatted
            throw new IllegalArgumentException();
        }
    }
    
    @Override
    public void close() {
        buffer = null;
    }

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        if(extent().contains(x, y)){
            return buffer.get(Math.abs(x - fromLongitude*3600) + Math.abs(y - (fromLatitude + 1)*3600) * 3601);
        }
        else{
            throw new IllegalArgumentException("not in the extent");
        }
    }

}
