/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.delor.graphprocessing;

import java.awt.image.BufferedImage;
import static pl.delor.graphprocessing.GP.getBlue;
import static pl.delor.graphprocessing.GP.getGreen;
import static pl.delor.graphprocessing.GP.getRed;
import static pl.delor.graphprocessing.GP.toRGB;

/**
 *
 * @author delor
 */
public class JPreviewPanelFilter extends JPreviewPanel {

    public JPreviewPanelFilter(String title, int[][] filter) {
        this.title = title;
        this.filter = filter;
    }

    private String title = null;

    @Override
    protected String title() {
        return title;
    }
    
    private int[][] filter = null;

    public int[][] getFilter(){
        return filter;
    }
    
    public void setFilter(int[][] filter){
        this.filter = filter;
        this.revalidate();
        this.repaint();
    }
    
    private int _weight = 0;
    private int _dx = 0;
    private int _dy = 0;
    private int _w = 0;
    private int _h = 0;

    private void initProcessing(BufferedImage input) {
        int w = 0;
        for (int i = 0; i < filter.length; i++) {
            for (int j = 0; j < filter[i].length; j++) {
                w += filter[i][j];
            }
        }
        _weight = (w == 0) ? 1 : w;
        _dy = (filter.length - 1) / 2;
        _dx = (filter[0].length - 1) / 2;
        _w = input.getWidth();
        _h = input.getHeight();
    }

    private int applyFilter(int x, int y, BufferedImage input) {
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;

        for (int fy = 0; fy < filter.length; fy++) {
            for (int fx = 0; fx < filter[fy].length; fx++) {
                int _x = x + fx - _dx;
                int _y = y + fy - _dy;
                if (_x < 0 || _x >= _w || _y < 0 || _y >= _h) {
                    /* outside of image */
                    sumR += filter[fy][fx] * 127;
                    sumG += filter[fy][fx] * 127;
                    sumB += filter[fy][fx] * 127;
                } else {
                    sumR += filter[fy][fx] * getRed(input.getRGB(_x, _y));
                    sumG += filter[fy][fx] * getGreen(input.getRGB(_x, _y));
                    sumB += filter[fy][fx] * getBlue(input.getRGB(_x, _y));
                }
            }
        }
        return toRGB(sumR / _weight, sumG / _weight, sumB / _weight);
    }

    @Override
    protected BufferedImage doProcessImage(BufferedImage input, BufferedImage output) {
        initProcessing(input);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                output.setRGB(x, y, applyFilter(x, y, input));
            }
        }
        return output;
    }

}
