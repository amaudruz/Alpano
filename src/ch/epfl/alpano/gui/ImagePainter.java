package ch.epfl.alpano.gui;

import java.awt.Color;

@FunctionalInterface
public interface ImagePainter {

    Color colorAt(int x, int y);
    
    static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter brightness, ChannelPainter opacity) {
        return (x,y) -> Color.hsb(hue.valueAt(x,y), saturation.valueAt(x,y), brightness.valueAt(x,y), opacity.valueAt(x,y));
    }
    
    static ImagePainter gray(ChannelPainter gray, ChannelPainter opacity) {
        return (x,y) -> Color.gray(gray.valueAt(x,y), opacity.valueAt(x,y));
    }
}
