package pl.delor.graphprocessing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import static java.lang.Math.pow;
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
        return (toConstraints(a) << 24) | (toConstraints(r) << 16) | (toConstraints(g) << 8) | toConstraints(b);
    }


    // sRGB luminance(Y) values
    final static double rY = 0.212655;
    final static double gY = 0.715158;
    final static double bY = 0.072187;

    //calculating luminance source: https://stackoverflow.com/a/13558570
// Inverse of sRGB "gamma" function. (approx 2.2)
    static double inv_gam_sRGB(int ic) {
        double c = ic / 255.0;
        if (c <= 0.04045) {
            return c / 12.92;
        } else {
            return pow(((c + 0.055) / (1.055)), 2.4);
        }
    }

// sRGB "gamma" function (approx 2.2)
    static int gam_sRGB(double v) {
        if (v <= 0.0031308) {
            v *= 12.92;
        } else {
            v = 1.055 * pow(v, 1.0 / 2.4) - 0.055;
        }
        return (int) (v * 255 + 0.5); // This is correct in C++. Other languages may not
        // require +0.5
    }

// GRAY VALUE ("brightness")
    public static int toLuminance(int r, int g, int b) {
        return gam_sRGB(
                rY * inv_gam_sRGB(r)
                + gY * inv_gam_sRGB(g)
                + bY * inv_gam_sRGB(b)
        );
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new MultimediaMainFrame().main(args);
    }
}
