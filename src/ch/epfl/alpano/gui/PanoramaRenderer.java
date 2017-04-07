package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;;

public interface PanoramaRenderer {
    
    static Image renderPanorama(Panorama panorama, ImagePainter painter) {
        int width = panorama.parameters().width();
        int height = panorama.parameters().height();
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                writer.setColor(x, y, painter.colorAt(x, y));
            }
        }
        
        return image;
    }
}
