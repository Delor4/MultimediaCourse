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
public class JPreviewPanelMorf extends JPreviewPanel {

    public JPreviewPanelMorf(int morf) {
        super();        
        this.morf = morf;
    }

    @Override
    protected String title() {
        return (morf == 1) ? "Erosion" : "Dilation";
    }
    
    private int morf = 1;
    private int _w = 0;
    private int _h = 0;
    private int[][] _in;
    private int[][] _out;
    
    protected void initProcessing(BufferedImage input) {
        _w = input.getWidth();
        _h = input.getHeight();
        _in = new int[_w][_h];
        _out = new int[_w][_h];
        
        for (int y = 0; y < _h; y++) {
            for (int x = 0; x < _w; x++) {
                int pixel = input.getRGB(x, y);
                _in[x][y] = ((getRed(pixel) + getGreen(pixel) + getBlue(pixel))/3 > 127)? 1 : 0;
                _out[x][y] = _in[x][y];
                
            }
        }        
    }
    protected void finishProcessing(BufferedImage output) {
        final int vals[] = new int[]{toRGB(0, 0, 0), toRGB(255, 255, 255)};
        for (int y = 0; y < _h; y++) {
            for (int x = 0; x < _w; x++) {
                output.setRGB(x, y, vals[_out[x][y]]);
            }
        }
    }
    
    final int _fx[] = new int[]{-1,  0, 0, 0, 1}; 
    final int _fy[] = new int[]{ 0, -1, 0, 1, 0}; 
    
    protected void applyMorf(int x, int y) {
        for (int i = 0; i < _fx.length; i++) {
            int _x = x + _fx[i];
            int _y = y + _fy[i];
            if (_x < 0 || _x >= _w || _y < 0 || _y >= _h) {
               continue;                   
            } else {
                _out[_x][_y] = morf;
            }
        }
    }

    @Override
    protected BufferedImage doProcessImage(BufferedImage input, BufferedImage output) {
        initProcessing(input);
        for (int y = 0; y < _h; y++) {
            for (int x = 0; x < _w; x++) {
                if(_in[x][y] == morf){
                    applyMorf(x, y);
                }
            }
        }
        finishProcessing(output);
        return output;
    }

}
