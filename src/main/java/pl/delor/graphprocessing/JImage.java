/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.delor.graphprocessing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import static pl.delor.graphprocessing.GP.getBlue;
import static pl.delor.graphprocessing.GP.getGreen;
import static pl.delor.graphprocessing.GP.getRed;
import static pl.delor.graphprocessing.GP.toRGB;

/**
 *
 * @author delor
 */
public class JImage extends javax.swing.JPanel {

    /**
     * Creates new form JImage
     */
    private BufferedImage image = null;
    private Integer avgBrightness = null;
    private Integer contrastVariance = null;
    private Integer contrastDynamic = null;

    public Integer getAvgBrightness() {
        if (image == null) {
            return null;
        }
        if (avgBrightness == null) {
            calculateAvgBrightness();
        }
        return avgBrightness;
    }

    public Integer getContrastVariance() {
        if (image == null) {
            return null;
        }
        if (contrastVariance == null) {
            calculateContrastVariance();
        }
        return contrastVariance;
    }

    public Integer getContrastDynamic() {
        if (image == null) {
            return null;
        }
        if (contrastDynamic == null) {
            calculateContrastDynamic();
        }
        return contrastDynamic;
    }

    public JImage() {
        initComponents();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.avgBrightness = null;
        this.contrastVariance = null;
        this.contrastDynamic = null;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Image imageResized = image.getScaledInstance(
                    this.getWidth(),
                    this.getHeight(),
                    java.awt.Image.SCALE_SMOOTH
            );
            g.drawImage(imageResized, 0, 0, this);        
        }
    }

    private void calculateAvgBrightness() {
        long sumRed = 0;
        long sumGreen = 0;
        long sumBlue = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                sumRed += getRed(pixel);
                sumGreen += getGreen(pixel);
                sumBlue += getBlue(pixel);
            }
        }
        long pixels = image.getHeight() * image.getWidth();
        avgBrightness = toRGB(
                (int) (sumRed / pixels),
                (int) (sumGreen / pixels),
                (int) (sumBlue / pixels)
        );
    }

    private void calculateContrastVariance() {
        if (avgBrightness == null) {
            calculateAvgBrightness();
        }

        long sumRed = 0;
        int avgRed = getRed(avgBrightness);
        long sumGreen = 0;
        int avgGreen = getGreen(avgBrightness);
        long sumBlue = 0;
        int avgBlue = getBlue(avgBrightness);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                sumRed += (getRed(pixel) - avgRed) * (getRed(pixel) - avgRed);
                sumGreen += (getGreen(pixel) - avgGreen) * (getGreen(pixel) - avgGreen);
                sumBlue += (getBlue(pixel) - avgBlue) * (getBlue(pixel) - avgBlue);
            }
        }
        long pixels = image.getHeight() * image.getWidth();
        contrastVariance = toRGB(
                (int) Math.sqrt(sumRed / pixels),
                (int) Math.sqrt(sumGreen / pixels),
                (int) Math.sqrt(sumBlue / pixels)
        );
    }

    private void calculateContrastDynamic() {
        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;
        int minRed = 255;
        int minGreen = 255;
        int minBlue = 255;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int red = getRed(pixel);
                maxRed = maxRed < red ? red : maxRed;
                minRed = minRed > red ? red : minRed;

                int g = getGreen(pixel);
                maxGreen = maxGreen < g ? g : maxGreen;
                minGreen = minGreen > g ? g : minGreen;

                int b = getBlue(pixel);
                maxBlue = maxBlue < b ? b : maxBlue;
                minBlue = minBlue > b ? b : minBlue;
            }
        }
        contrastDynamic = toRGB((maxRed - minRed), (maxGreen - minGreen), (maxBlue - minBlue));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
