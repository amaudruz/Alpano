package ch.epfl.alpano.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

public final class LabelizerTest {

   
    public static void main(String args[]) throws IOException {
        ContinuousElevationModel cem = new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("N46E007.hgt")));
        List<Summit> summit = GazetteerParser.readSummitsFrom(new File("alps.txt"));
        System.out.println(summit.isEmpty());
        Labelizer labelizer = new Labelizer(cem, summit);
        List<Summit> l = labelizer.visibleSummits(PredefinedPanoramas.NIESEN.panoramaDisplayParameters());
        l.forEach(System.out::println);
        System.out.println(l.isEmpty());
    }
    
}
