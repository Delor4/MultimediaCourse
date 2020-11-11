package pl.delor.graphprocessing;
import java.util.Dictionary;
import java.util.Hashtable;
import java.awt.image.BufferedImage;
import static pl.delor.graphprocessing.GP.getBlue;
import static pl.delor.graphprocessing.GP.getGreen;
import static pl.delor.graphprocessing.GP.getRed;
import static pl.delor.graphprocessing.GP.toRGB;
import java.awt.Component;
import javax.swing.JLabel;
/**
 *
 * @author delor
 */
public class JPreviewPanelBinary extends JPreviewPanel {

    /**
     * Creates new form JPreviewPanelBinary
     */
    public JPreviewPanelBinary() {
        initComponents();
        
        Dictionary<Integer, Component> labelTable = new Hashtable<Integer, Component>();
        
        labelTable.put(0, new JLabel("0"));
        labelTable.put(50, new JLabel("50"));
        labelTable.put(100, new JLabel("100"));
        labelTable.put(150, new JLabel("150"));
        labelTable.put(200, new JLabel("200"));
        labelTable.put(250, new JLabel("Max"));

        jSliderBinarization.setLabelTable(labelTable);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSliderBinarization = new javax.swing.JSlider();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jSliderBinarization.setMajorTickSpacing(50);
        jSliderBinarization.setMaximum(250);
        jSliderBinarization.setMinorTickSpacing(5);
        jSliderBinarization.setPaintLabels(true);
        jSliderBinarization.setPaintTicks(true);
        jSliderBinarization.setValue(125);
        jSliderBinarization.setRequestFocusEnabled(false);
        jSliderBinarization.setValueIsAdjusting(true);
        jSliderBinarization.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderBinarizationStateChanged(evt);
            }
        });
        add(jSliderBinarization);
    }// </editor-fold>//GEN-END:initComponents

    private void jSliderBinarizationStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderBinarizationStateChanged
        super.processImage();
    }//GEN-LAST:event_jSliderBinarizationStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider jSliderBinarization;
    // End of variables declaration//GEN-END:variables

    @Override
    protected String title() {
        return "Binarization";
    }

    @Override
    protected BufferedImage doProcessImage(BufferedImage input, BufferedImage output) {
        int step = jSliderBinarization.getValue();
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int v = ((getRed(pixel) + getGreen(pixel) + getBlue(pixel))/3) < step ? 0 : 255;
                output.setRGB(x, y, toRGB(v, v, v));
            }
        }
        return output;
    }

}
