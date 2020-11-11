package pl.delor.graphprocessing;

import java.awt.image.BufferedImage;

/**
 *
 * @author delor
 */
public class JPreviewPanelProcess extends JPreviewPanel {

    @FunctionalInterface
    public interface ProcessPixel {

        int apply(int pixel);
    }

    public JPreviewPanelProcess(String title, ProcessPixel f) {
        this.title = title;
        this.f = f;
    }

    private String title = null;

    @Override
    protected String title() {
        return title;
    }
    private ProcessPixel f = null;

    @Override
    protected BufferedImage doProcessImage(BufferedImage input, BufferedImage output) {
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                output.setRGB(x, y, f.apply(input.getRGB(x, y)));
            }
        }
        return output;
    }

}
