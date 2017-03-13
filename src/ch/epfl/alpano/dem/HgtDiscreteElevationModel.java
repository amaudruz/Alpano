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

public final class HgtDiscreteElevationModel implements DiscreteElevationModel {

    private ShortBuffer buffer;
    private final Interval2D extent;
    private int fromLa;
    private int fromLo;
    
    public HgtDiscreteElevationModel(File file) throws IOException{
        String fileName = file.getName();
        
        if(fileName.charAt(0) != 'N' && fileName.charAt(0) != 'S'){
            throw new IllegalArgumentException("Should begin by N or S");
        }
        if(fileName.charAt(3) != 'E' && fileName.charAt(3) != 'W'){
            throw new IllegalArgumentException("Should be E or W");
        }
        try{
            fromLa = Integer.parseInt(fileName.substring(1, 3));
            if(fileName.charAt(0) != 'N'){
                fromLa = (int) (-fromLa);
            }
            fromLo = Integer.parseInt(fileName.substring(4, 7));
            if(fileName.charAt(3) == 'W'){
                fromLo = (int) (-fromLo);
            }

        }
        catch(NumberFormatException e){
            throw new IllegalArgumentException();
        }
        
        if(fileName.substring(7, 10).equals(".hgt")){
            throw new IllegalArgumentException("should be a .hgt");
        }
        
        extent = new Interval2D(new Interval1D(fromLo * 3600, (fromLo + 1) * 3600), new Interval1D(fromLa * 3600, (fromLa + 1) * 3600));
       
        try(FileInputStream fileStream = new FileInputStream(file)){
            long length = file.length();
            if(length != 25934402){
                throw new IllegalArgumentException("wrong length");
            }
            buffer = fileStream.getChannel().map(MapMode.READ_ONLY, 0, length).asShortBuffer();
        }
    }
    
    @Override
    public void close(){
        buffer = null;
    }

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        int index = (x - fromLo*3600)  + ((fromLa + 1)*3600 - y) * 3601;
        return buffer.get(index);
    }

}
