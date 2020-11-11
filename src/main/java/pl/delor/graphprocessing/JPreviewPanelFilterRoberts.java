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
public class JPreviewPanelFilterRoberts extends JPreviewPanelFilter {

    public JPreviewPanelFilterRoberts(String title) {
        super(title, new int[][]{{1}});
    }
   
    private int _w = 0;
    private int _h = 0;
    
    @Override
    protected void initProcessing(BufferedImage input) {
        _w = input.getWidth();
        _h = input.getHeight();
    }

    @Override
    protected int applyFilter(int x, int y, BufferedImage input) {
        if(x == (_w - 1) || y == (_h - 1)) {
            /* bottom or right side of image */
            return toRGB(0, 0, 0);
        }
        
        int p1 = input.getRGB(x, y);
        int p2 = input.getRGB(x + 1, y);
        int p3 = input.getRGB(x, y + 1);
        int sumR = Math.abs(getRed(p1) - getRed(p2)) + Math.abs(getRed(p1) - getRed(p3));
        int sumG = Math.abs(getGreen(p1) - getGreen(p2)) + Math.abs(getGreen(p1) - getGreen(p3));
        int sumB = Math.abs(getBlue(p1) - getBlue(p2)) + Math.abs(getBlue(p1) - getBlue(p3));
        
        return toRGB(sumR, sumG, sumB);
    }
}
