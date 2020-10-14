/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.delor.graphprocessing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Base64;
import javax.swing.ImageIcon;

/**
 *
 * @author delor
 */
public class GP {

    public static ImageIcon getIconFromBase64(String s) {
        byte[] imageBytes = Base64.getDecoder().decode(s.getBytes());
        return new ImageIcon(imageBytes);
    }

    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public static String getFileExtension(File file) {
        if (file == null) {
            return "";
        }
        String name = file.getName();
        int i = name.lastIndexOf('.');
        String ext = i > 0 ? name.substring(i + 1) : "";
        return ext;
    }

    public static String getImageExtension(String ext) {
        ext = ext.toLowerCase();
        switch (ext) {
            case "jpg", "jpeg", "gif", "png" -> {
                return ext;
            }
        }
        return "jpeg";
    }

    public static int getAlfa(int pixel) {
        return (pixel & 0xff000000) >>> 24;
    }

    public static int getRed(int pixel) {
        return (pixel & 0x00ff0000) >>> 16;
    }

    public static int getGreen(int pixel) {
        return (pixel & 0x0000ff00) >>> 8;
    }

    public static int getBlue(int pixel) {
        return (pixel & 0x000000ff);
    }

    public static int toRGB(int r, int g, int b) {
        return toARGB(r, g, b, 0xff);
    }

    public static int toConstraints(int val) {
        return (val < 0) ? 0 : (val > 255 ? 255 : val);
    }

    public static int toARGB(int r, int g, int b, int a) {
        return ((((toConstraints(a) << 8 | toConstraints(r)) << 8) | toConstraints(g)) << 8) | toConstraints(b);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new MultimediaMainFrame().main(args);
    }
}
