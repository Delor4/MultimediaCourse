package pl.delor.graphprocessing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import static pl.delor.graphprocessing.GP.getBlue;
import static pl.delor.graphprocessing.GP.getGreen;
import static pl.delor.graphprocessing.GP.getRed;
import static pl.delor.graphprocessing.GP.toRGB;

/**
 *
 * @author delor
 */
public class JImage extends javax.swing.JPanel {

    private BufferedImage image = null;
    private Integer avgBrightness = null;
    private Integer contrastVariance = null;
    private Integer contrastDynamic = null;
    private int histogram[][] = null;
    private long[][] cdf = null;
    private List<int[][]> projections = null;

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

    public int[][] getHistogram() {
        if (image == null) {
            return null;
        }
        if (histogram == null) {
            calculateHistogram();
        }
        return histogram;
    }

    public long[][] getCDF() {
        if (image == null) {
            return null;
        }
        if (cdf == null) {
            calculateCDF();
        }
        return cdf;
    }

    public List<int[][]> getProjections() {
        if (image == null) {
            return null;
        }
        if (projections == null) {
            calculateProjections();
        }
        return projections;
    }

    /**
     * Creates new form JImage
     */
    public JImage() {
        initComponents();
    }

    @FunctionalInterface
    public interface ApplyPixel {

        void apply(int pixel);
    }

    void forEachPixel(ApplyPixel f) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                f.apply(image.getRGB(x, y));
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.avgBrightness = null;
        this.contrastVariance = null;
        this.contrastDynamic = null;
        this.histogram = null;
        this.cdf = null;
        this.projections = null;
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
        final long sums[] = new long[3];
        forEachPixel((pixel) -> {
            sums[0] += getRed(pixel);
            sums[1] += getGreen(pixel);
            sums[2] += getBlue(pixel);
        });

        long pixels = image.getHeight() * image.getWidth();
        avgBrightness = toRGB(
                (int) (sums[0] / pixels),
                (int) (sums[1] / pixels),
                (int) (sums[2] / pixels)
        );
    }

    private void calculateContrastVariance() {
        if (avgBrightness == null) {
            calculateAvgBrightness();
        }

        final long sums[] = new long[3];

        final int avgRed = getRed(avgBrightness);
        final int avgGreen = getGreen(avgBrightness);
        final int avgBlue = getBlue(avgBrightness);

        forEachPixel((pixel) -> {
            sums[0] += (getRed(pixel) - avgRed) * (getRed(pixel) - avgRed);
            sums[1] += (getGreen(pixel) - avgGreen) * (getGreen(pixel) - avgGreen);
            sums[2] += (getBlue(pixel) - avgBlue) * (getBlue(pixel) - avgBlue);
        });
        long pixels = image.getHeight() * image.getWidth();
        contrastVariance = toRGB(
                (int) Math.sqrt(sums[0] / pixels),
                (int) Math.sqrt(sums[1] / pixels),
                (int) Math.sqrt(sums[2] / pixels)
        );
    }

    private void calculateContrastDynamic() {
        final int min[] = new int[]{255, 255, 255};
        final int max[] = new int[]{0, 0, 0};

        forEachPixel((pixel) -> {
            int r = getRed(pixel);
            if (max[0] < r) {
                max[0] = r;
            }
            if (min[0] > r) {
                min[0] = r;
            }

            int g = getGreen(pixel);
            if (max[1] < g) {
                max[1] = g;
            }
            if (min[1] > g) {
                min[1] = g;
            }

            int b = getBlue(pixel);
            if (max[2] < b) {
                max[2] = b;
            }
            if (min[2] > b) {
                min[2] = b;
            }
        });
        contrastDynamic = toRGB(max[0] - min[0], max[1] - min[1], max[2] - min[2]);
    }

    private void calculateHistogram() {
        histogram = new int[3][256];
        forEachPixel((pixel) -> {
            histogram[0][getRed(pixel)]++;
            histogram[1][getGreen(pixel)]++;
            histogram[2][getBlue(pixel)]++;
        });
    }

    private void calculateCDF() {
        if (histogram == null) {
            calculateHistogram();
        }
        cdf = new long[histogram.length][histogram[0].length];
        for (int c = 0; c < cdf.length; c++) {
            long val = 0;
            for (int i = 0; i < histogram[c].length; i++) {
                val += histogram[c][i];
                cdf[c][i] = val;
            }
        }
    }

    private void calculateProjections() {
        projections = new ArrayList<>();

        int projHoriz[][] = new int[3][image.getWidth()];
        int projVert[][] = new int[3][image.getHeight()];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                if (getRed(pixel) > 127) {
                    projHoriz[0][x]++;
                    projVert[0][y]++;
                }
                if (getGreen(pixel) > 127) {
                    projHoriz[1][x]++;
                    projVert[1][y]++;
                }
                if (getBlue(pixel) > 127) {
                    projHoriz[2][x]++;
                    projVert[2][y]++;
                }
            }
        }
        projections.add(projHoriz);
        projections.add(projVert);
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
